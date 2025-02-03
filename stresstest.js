const axios = require('axios');
const ordersToRun = 150000;

function generateRandomOrder() {
    const codigoOrder = Math.random().toString(36).substring(2, 10).toUpperCase();
    const produtosList = [];
    const numProdutos = Math.floor(Math.random() * 3) + 1;

    for (let i = 0; i < numProdutos; i++) {
        produtosList.push({
            idProduto: Math.floor(Math.random() * 100) + 1,
            valor: parseFloat((Math.random() * 100).toFixed(2))
        });
    }

    return {
        codigoOrder,
        produtosList
    };
}

async function sendOrder() {
    const order = generateRandomOrder();
    try {
        const response = await axios.post('http://localhost:8080/orders', order);
        console.log(`Order ${JSON.stringify(order)} sent successfully:`, response.data);
    } catch (error) {
        console.error(`Error sending order ${JSON.stringify(order)}:`, error.message);
    }
}

async function verifyOrders() {
    try {
        const response = await axios.get('http://localhost:8080/orders');
        const orders = response.data;
        console.log(`Total orders in database: ${orders.length}`);
        if (orders.length >= ordersToRun) {
            console.log(`Stress test passed: ${ordersToRun} or more orders found in the database.`);
        } else {
            console.log(`Stress test failed: Less than ${ordersToRun} orders found in the database.`);
        }
    } catch (error) {
        console.error('Error verifying orders:', error.message);
    }
}

async function stressTest() {
    for (let i = 0; i < ordersToRun; i++) {
        await sendOrder();
        //await new Promise(resolve => setTimeout(resolve, 10)); // Pause for 500ms
    }
    setTimeout(verifyOrders, 2000); // Wait 5 seconds before verifying
}

stressTest();
