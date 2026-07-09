const fs = require('fs');
const express = require('express');
const { v4: uuidv4 } = require('uuid');

const data = JSON.parse(fs.readFileSync('data.json', 'UTF-8'));
console.log('Loaded cards:', data);

const server = express();
server.use(express.json());

server.post(['/payment', '/credit'], (req, res) => {
  console.log(`Incoming request: ${req.path} ${JSON.stringify(req.body)}`);
  const { body: { number } } = req;

  const [item] = data.filter(o => o.number === number);
  if (item === undefined) {
    res.status(400).end();
    return;
  }

  res.send({
    id: uuidv4(),
    status: item.status,
  });
});

server.get('/health', (req, res) => {
  res.json({ status: 'ok' });
});

server.listen(process.env.PORT || 9999, () => {
  console.log(`Gate simulator running on port ${process.env.PORT || 9999}`);
});