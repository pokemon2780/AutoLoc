package tn.esprit.autoloc.web;

import com.jayway.jsonpath.JsonPath;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.autoloc.repository.IClientRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class Atelier6ApiTest {
    @Autowired private MockMvc mvc;
    @Autowired private IClientRepository clients;

    static Stream<Arguments> resources() {
        return Stream.of(
            Arguments.of("agences", "idAgence", "{\"nom\":\"Agence test\",\"ville\":\"Tunis\",\"adresse\":\"Rue test\",\"telephone\":\"20123456\"}"),
            Arguments.of("clients", "idClient", "{\"nom\":\"Test\",\"prenom\":\"Alice\",\"email\":\"alice@example.com\",\"telephone\":\"20123456\",\"numPermis\":\"P123\",\"dateInscription\":\"2020-01-01\"}"),
            Arguments.of("vehicules", "idVehicule", vehicle("CRUD-TEST")),
            Arguments.of("employes", "idEmploye", "{\"nom\":\"Test\",\"prenom\":\"Alice\",\"role\":\"AGENT\"}"),
            Arguments.of("equipements", "idEquipement", "{\"libelle\":\"GPS\"}"),
            Arguments.of("reservations", "idReservation", "{\"dateDebut\":\"2030-01-01\",\"dateFin\":\"2030-01-02\",\"statut\":\"EN_ATTENTE\"}"),
            Arguments.of("maintenances", "idMaintenance", "{\"dateDebut\":\"2030-01-01\",\"dateFin\":\"2030-01-02\",\"description\":\"Révision\"}"),
            Arguments.of("contrats", "idContrat", contrat())
        );
    }

    @ParameterizedTest
    @MethodSource("resources")
    void allWritableResourcesReturnDtos(String resource, String idField, String body) throws Exception {
        long id = create(resource, idField, body);
        mvc.perform(get("/" + resource + "/" + id)).andExpect(status().isOk())
                .andExpect(jsonPath("$." + idField).value(id))
                .andExpect(jsonPath("$.hibernateLazyInitializer").doesNotExist());
        mvc.perform(get("/" + resource)).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
        mvc.perform(put("/" + resource + "/" + id).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andExpect(jsonPath("$." + idField).value(id));
        mvc.perform(delete("/" + resource + "/" + id)).andExpect(status().isNoContent())
                .andExpect(content().string(""));
        mvc.perform(get("/" + resource + "/" + id)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statut").value(404));
    }

    @Test
    void invalidClientReportsFieldsWithoutPersisting() throws Exception {
        long count = clients.count();
        mvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nom\":\" \",\"email\":\"incorrect\"}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.statut").value(400))
                .andExpect(jsonPath("$.erreurs.nom").exists()).andExpect(jsonPath("$.erreurs.email").exists())
                .andExpect(jsonPath("$.chemin").value("/clients"));
        assertEquals(count, clients.count());
    }

    @Test
    void invalidTypesEnumsAndDatesReturnUniformErrors() throws Exception {
        for (String body : new String[]{"{", vehicle("BAD-ENUM").replace("CITADINE", "INCONNU"),
                vehicle("BAD-PRICE").replace("40.00", "-5.00")}) {
            mvc.perform(post("/vehicules").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.statut").value(400))
                    .andExpect(jsonPath("$.horodatage").exists());
        }
        mvc.perform(post("/reservations").contentType(MediaType.APPLICATION_JSON)
                .content("{\"dateDebut\":\"2030-02-02\",\"dateFin\":\"2030-01-01\",\"statut\":\"EN_ATTENTE\"}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.erreurs.periodeValide").exists());
    }

    @Test
    void invalidIdentifiersAndUnsupportedMethodsKeepHttpStatus() throws Exception {
        for (String id : new String[]{"abc", "0", "-1"}) {
            mvc.perform(get("/clients/" + id)).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statut").value(400));
        }
        mvc.perform(patch("/clients/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isMethodNotAllowed()).andExpect(jsonPath("$.statut").value(405));
        mvc.perform(post("/clients").contentType(MediaType.TEXT_PLAIN).content("test"))
                .andExpect(status().isUnsupportedMediaType()).andExpect(jsonPath("$.statut").value(415));
    }

    @Test
    void relationIdentifiersAreResolvedAndReturnedWithoutNestedEntities() throws Exception {
        long agence = create("agences", "idAgence", "{\"nom\":\"Relations\",\"ville\":\"Tunis\",\"adresse\":\"Rue\",\"telephone\":\"20123456\"}");
        long equipment = create("equipements", "idEquipement", "{\"libelle\":\"Siège enfant\"}");
        String body = vehicle("RELATIONS").replace("}", ",\"agenceId\":" + agence + ",\"equipementIds\":[" + equipment + "]}");
        long id = create("vehicules", "idVehicule", body);
        mvc.perform(get("/vehicules/" + id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.agenceId").value(agence))
                .andExpect(jsonPath("$.equipementIds[0]").value(equipment))
                .andExpect(jsonPath("$.agence").doesNotExist()).andExpect(jsonPath("$.equipements").doesNotExist());
        mvc.perform(put("/vehicules/" + id).contentType(MediaType.APPLICATION_JSON).content(vehicle("RELATIONS")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.equipementIds").isEmpty());
        mvc.perform(post("/vehicules").contentType(MediaType.APPLICATION_JSON)
                .content(vehicle("MISSING-REF").replace("}", ",\"agenceId\":9223372036854775807}")))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.statut").value(404));
    }

    @Test
    void duplicateRegistrationAndReferencedAgencyReturn409() throws Exception {
        long agence = create("agences", "idAgence", "{\"nom\":\"FK\",\"ville\":\"Tunis\",\"adresse\":\"Rue\",\"telephone\":\"20123456\"}");
        String body = vehicle("DUPLICATE").replace("}", ",\"agenceId\":" + agence + "}");
        create("vehicules", "idVehicule", body);
        mvc.perform(post("/vehicules").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.statut").value(409))
                .andExpect(jsonPath("$.trace").doesNotExist());
        mvc.perform(delete("/agences/" + agence)).andExpect(status().isConflict())
                .andExpect(jsonPath("$.statut").value(409));
    }

    @Test
    void contractPaymentsUseValidatedDtosAndRespectOrphanRemoval() throws Exception {
        long id = create("contrats", "idContrat", contrat());
        String response = mvc.perform(post("/contrats/" + id + "/paiements")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"montant\":50,\"datePaiement\":\"2026-01-01\",\"modePaiement\":\"CARTE\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.paiements[0].contratId").value(id))
                .andExpect(jsonPath("$.paiements[0].contrat").doesNotExist())
                .andReturn().getResponse().getContentAsString();
        long payment = ((Number) JsonPath.read(response, "$.paiements[0].idPaiement")).longValue();
        mvc.perform(get("/paiements/" + payment)).andExpect(status().isOk())
                .andExpect(jsonPath("$.contratId").value(id));
        mvc.perform(get("/paiements")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
        mvc.perform(delete("/contrats/" + id + "/paiements/" + payment)).andExpect(status().isOk())
                .andExpect(jsonPath("$.paiements").isEmpty());
        mvc.perform(get("/paiements/" + payment)).andExpect(status().isNotFound());
        mvc.perform(post("/contrats").contentType(MediaType.APPLICATION_JSON)
                .content(contrat().replace("}", ",\"paiements\":[{\"montant\":-1}]}")))
                .andExpect(status().isBadRequest());
    }

    private long create(String resource, String idField, String body) throws Exception {
        String response = mvc.perform(post("/" + resource).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(response, "$." + idField)).longValue();
    }

    private static String vehicle(String plate) {
        return "{\"immatriculation\":\"" + plate + "\",\"marque\":\"Toyota\",\"modele\":\"Yaris\",\"categorie\":\"CITADINE\",\"tarifJournalier\":40.00,\"statut\":\"DISPONIBLE\"}";
    }

    private static String contrat() {
        return "{\"dateSignature\":\"2026-01-01\",\"montantTotal\":150,\"valide\":true}";
    }
}
