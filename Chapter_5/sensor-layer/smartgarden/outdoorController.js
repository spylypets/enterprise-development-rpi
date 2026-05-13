const express = require('express'),
      http = require('http'),
      app = express(),
      server = new http.Server(app),
      relayService = require('./services/relayService.js'),
      mqttService = require('./services/mqttService.js');

app.use((req, res, next) => {
    res.append('Access-Control-Allow-Origin', ['*']);
    res.append('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
    res.append('Access-Control-Allow-Headers', 'Content-Type');
    next();
});

relayService.init('w', 'l');
mqttService.init(relayService);

   app.get("/water/:action", (req, res) => {

        switch(req.params.action) {

           case 'on':
                relayService.switchOn('w');
                res.send(relayService.getStatus('w').toString());
                break;
           case 'off':
                relayService.switchOff('w');
                res.send(relayService.getStatus('w').toString());
                break;
           case 'status':
                res.send(relayService.getStatus('w').toString());
                break;
           default:
                console.log('Unknown command: ' + req.params.action);
                res.sendStatus(400);
        }
  });

  app.get("/light/:action", (req, res) => {

        switch(req.params.action) {

           case 'on':
                relayService.switchOn('l');
                res.send(relayService.getStatus('l').toString());
                break;
           case 'off':
                relayService.switchOff('l');
                res.send(relayService.getStatus('l').toString());
                break;
           case 'status':
                res.send(relayService.getStatus('l').toString());
                break;
           default:
                console.log('Unknown command: ' + req.params.action);
                res.sendStatus(400);
        }
  });

  server.listen(3030, () => {
    console.log('Outdoor controller listens to port 3030');
  });
