const express = require('express');
const app = express();
const PORT = 3000;

app.use(express.json()); // Middleware for parsing JSON

// Store multiple integers
const data = {
    manyPlayers: 0,
    p1x: 0,
    p1y: 0,  
    p2x: 0, 
    p2y: 0, 
    p3x: 0,
    p3y: 0,  
    p4x: 0, 
    p4y: 0,
};

// Endpoint to get a specific variable by key
app.get('/data/:key', (req, res) => {
    const key = req.params.key;
    console.log(`Received GET request for key: ${key}`);
    if (data.hasOwnProperty(key)) {
        res.json({ [key]: data[key] });
    } else {
        res.status(404).send('Key not found');
    }
});

// Endpoint to update a specific variable
app.post('/data', (req, res) => {
    const { key, value } = req.body;
    console.log('Received POST request with data:', req.body);
    if (typeof key === 'string' && typeof value === 'number') {
        data[key] = value;
        res.status(200).send('Value updated successfully');
    } else {
        res.status(400).send('Invalid data format');
    }
});

app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});
