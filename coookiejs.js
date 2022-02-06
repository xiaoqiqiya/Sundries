
const isRequest = typeof $request != "undefined"
if（isRequest ）{

 GetCookie()
}else{
}
function GetCookie() {
  const req = $request;
  if (req.method != 'OPTIONS' && req.headers) {
    const CV = (req.headers['Cookie'] || req.headers['cookie'] || '');
    const ckItems = CV.match(/(pt_key|pt_pin)=.+?;/g);
    if (ckItems && ckItems.length == 2) {
		$store.delete('qqjd') 
		$store.set('qqjd',  ckItems.join('')) 
		
	}
  }
}
