"""Capture a running AutoLoc test instance. Never substitutes mock responses."""
import json
import os
import time
from pathlib import Path
from playwright.sync_api import sync_playwright

base = os.environ.get('AUTOLOC_BASE_URL', 'http://localhost:8080').rstrip('/')
out = Path('docs/captures/atelier6')
out.mkdir(parents=True, exist_ok=True)
with sync_playwright() as p:
    request = p.request.new_context(base_url=base)
    for attempt in range(60):
        try:
            if request.get('/v3/api-docs').ok:
                break
        except Exception:
            pass
        time.sleep(2)
    else:
        raise RuntimeError('AutoLoc did not start; inspect application logs')
    browser = p.chromium.launch()
    page = browser.new_page(viewport={'width': 1440, 'height': 1100})
    page.goto(base + '/swagger-ui/index.html')
    page.locator('.opblock').first.wait_for()
    page.screenshot(path=str(out / '01-swagger-dto.png'), full_page=True)
    client = {'nom': 'Demo', 'prenom': 'Alice', 'email': 'alice@example.com',
              'telephone': '20123456', 'numPermis': 'DEMO-001', 'dateInscription': '2020-01-01'}
    created = request.post('/clients', data=client)
    assert created.status == 201, created.text()
    cid = created.json()['idClient']
    evidence = [{'method': 'POST', 'path': '/clients', 'status': created.status, 'response': created.json()}]
    # A real browser GET displays the response from the running server.
    response = page.goto(base + '/clients/' + str(cid))
    assert response.status == 200
    page.screenshot(path=str(out / '02-client-dto-200.png'), full_page=True)
    page.goto(base + '/swagger-ui/index.html')
    operation = page.locator('.opblock-post').filter(has=page.locator('.opblock-summary-path', has_text='/clients'))
    operation.locator('.opblock-summary').click()
    operation.get_by_role('button', name='Try it out', exact=True).click()
    operation.locator('textarea').fill('{"nom":"","email":"incorrect"}')
    with page.expect_response(lambda response: response.url.endswith('/clients') and response.request.method == 'POST') as event:
        operation.get_by_role('button', name='Execute', exact=True).click()
    invalid = event.value
    assert invalid.status == 400, invalid.text()
    operation.locator('.responses-inner').wait_for()
    page.screenshot(path=str(out / '03-validation-400.png'), full_page=True)
    evidence.append({'method': 'POST', 'path': '/clients', 'status': invalid.status, 'response': invalid.json()})
    response = page.goto(base + '/clients/9223372036854775807')
    assert response.status == 404
    page.screenshot(path=str(out / '04-erreur-404.png'), full_page=True)
    evidence.append({'method': 'GET', 'path': '/clients/9223372036854775807', 'status': 404, 'response': response.json()})
    (out / 'execution.json').write_text(json.dumps(evidence, indent=2, ensure_ascii=False), encoding='utf-8')
    assert request.delete('/clients/' + str(cid)).status == 204
    browser.close()
