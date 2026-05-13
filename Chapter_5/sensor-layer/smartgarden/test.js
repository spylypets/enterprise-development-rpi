const express = require('express'),
      http = require('http'),
      app = express(),
      server = new http.Server(app);

app.use((req, res, next) => {
    res.append('Access-Control-Allow-Origin', ['*']);
    res.append('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
    res.append('Access-Control-Allow-Headers', 'Content-Type');
    next();
});

  app.get("/water/on", (req, res) => {
    console.log("Watering switched on");
  });
  app.get("/water/off", (req, res) => {
    console.log("Watering switched off");
  });
  app.get("/water/status", (req, res) => {
    console.log("TODO return the watering relay status");
  });
  app.get("/light/on", (req, res) => {
    console.log("Light switched on");
  });
  app.get("/light/off", (req, res) => {
    console.log("Light switched off");
  });
  app.get("/light/status", (req, res) => {
    console.log("TODO return the light relay status");
  });

 server.listen(3030, () => {console.log("Outdoor controller listens to port 3030")});
