var express = require('express');
var morgan = require('morgan')
var app = express();
var fs = require('fs')
var accessLogStream = fs.createWriteStream(__dirname + '/access.log', {flags: 'a'})

//setup the logger
app.use(morgan('combined', {stream: accessLogStream}))
app.use('/game', express.static(__dirname + '/public'));

var server = app.listen(3000, function () {
  var host = server.address().address;
  var port = server.address().port;

  console.log('Node app is up and running to serve static HTML content.');
});