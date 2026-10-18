import http from 'k6/http';
import { check } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://host.docker.internal:8082';

export const options = {
    vus: 100,          // 100 "clientes" ao mesmo tempo
    duration: '30s',
};

export default function () {
    const res = http.post(
        `${BASE_URL}/v1/api/orders`,
        JSON.stringify({ items: [{ sku: 'CABO-HDMI', quantity: 1 }] }),
        { headers: { 'Content-Type': 'application/json' } }
    );
    check(res, {
        'status 201': (r) => r.status === 201,
        'pedido confirmado': (r) => r.json('status') === 'CONFIRMED',
    });
}
