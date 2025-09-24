const fs = require('fs');
console.log('Before file read');
fs.readFile('myfile.txt', 'utf8', (err, data) => {
  if (err) throw err;
  console.log('File contents:', data);
});
console.log('After file read');