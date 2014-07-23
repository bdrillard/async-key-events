;(function(){
function a() {
  return document.write("Hello, world!")
}
var b = ["async_key_events", "client", "init"], c = this;
b[0] in c || !c.execScript || c.execScript("var " + b[0]);
for(var d;b.length && (d = b.shift());) {
  b.length || void 0 === a ? c = c[d] ? c[d] : c[d] = {} : c[d] = a
}
;
})();
