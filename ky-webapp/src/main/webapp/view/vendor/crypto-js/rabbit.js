!function(e,r,i){"object"==typeof exports?module.exports=exports=r(require("./core"),require("./enc-base64"),require("./md5"),require("./evpkdf"),require("./cipher-core")):"function"==typeof define&&define.amd?define(["./core","./enc-base64","./md5","./evpkdf","./cipher-core"],r):r(e.CryptoJS)}(this,function(e){return function(){function r(){for(var e=this._X,r=this._C,i=0;i<8;i++)s[i]=r[i];r[0]=r[0]+1295307597+this._b|0,r[1]=r[1]+3545052371+(r[0]>>>0<s[0]>>>0?1:0)|0,r[2]=r[2]+886263092+(r[1]>>>0<s[1]>>>0?1:0)|0,r[3]=r[3]+1295307597+(r[2]>>>0<s[2]>>>0?1:0)|0,r[4]=r[4]+3545052371+(r[3]>>>0<s[3]>>>0?1:0)|0,r[5]=r[5]+886263092+(r[4]>>>0<s[4]>>>0?1:0)|0,r[6]=r[6]+1295307597+(r[5]>>>0<s[5]>>>0?1:0)|0,r[7]=r[7]+3545052371+(r[6]>>>0<s[6]>>>0?1:0)|0,this._b=r[7]>>>0<s[7]>>>0?1:0;for(var i=0;i<8;i++){var t=e[i]+r[i],o=65535&t,a=t>>>16,c=((o*o>>>17)+o*a>>>15)+a*a,n=((4294901760&t)*t|0)+((65535&t)*t|0);f[i]=c^n}e[0]=f[0]+(f[7]<<16|f[7]>>>16)+(f[6]<<16|f[6]>>>16)|0,e[1]=f[1]+(f[0]<<8|f[0]>>>24)+f[7]|0,e[2]=f[2]+(f[1]<<16|f[1]>>>16)+(f[0]<<16|f[0]>>>16)|0,e[3]=f[3]+(f[2]<<8|f[2]>>>24)+f[1]|0,e[4]=f[4]+(f[3]<<16|f[3]>>>16)+(f[2]<<16|f[2]>>>16)|0,e[5]=f[5]+(f[4]<<8|f[4]>>>24)+f[3]|0,e[6]=f[6]+(f[5]<<16|f[5]>>>16)+(f[4]<<16|f[4]>>>16)|0,e[7]=f[7]+(f[6]<<8|f[6]>>>24)+f[5]|0}var i=e,t=i.lib,o=t.StreamCipher,a=i.algo,c=[],s=[],f=[],n=a.Rabbit=o.extend({_doReset:function(){for(var e=this._key.words,i=this.cfg.iv,t=0;t<4;t++)e[t]=16711935&(e[t]<<8|e[t]>>>24)|4278255360&(e[t]<<24|e[t]>>>8);var o=this._X=[e[0],e[3]<<16|e[2]>>>16,e[1],e[0]<<16|e[3]>>>16,e[2],e[1]<<16|e[0]>>>16,e[3],e[2]<<16|e[1]>>>16],a=this._C=[e[2]<<16|e[2]>>>16,4294901760&e[0]|65535&e[1],e[3]<<16|e[3]>>>16,4294901760&e[1]|65535&e[2],e[0]<<16|e[0]>>>16,4294901760&e[2]|65535&e[3],e[1]<<16|e[1]>>>16,4294901760&e[3]|65535&e[0]];this._b=0;for(var t=0;t<4;t++)r.call(this);for(var t=0;t<8;t++)a[t]^=o[t+4&7];if(i){var c=i.words,s=c[0],f=c[1],n=16711935&(s<<8|s>>>24)|4278255360&(s<<24|s>>>8),h=16711935&(f<<8|f>>>24)|4278255360&(f<<24|f>>>8),v=n>>>16|4294901760&h,b=h<<16|65535&n;a[0]^=n,a[1]^=v,a[2]^=h,a[3]^=b,a[4]^=n,a[5]^=v,a[6]^=h,a[7]^=b;for(var t=0;t<4;t++)r.call(this)}},_doProcessBlock:function(e,i){var t=this._X;r.call(this),c[0]=t[0]^t[5]>>>16^t[3]<<16,c[1]=t[2]^t[7]>>>16^t[5]<<16,c[2]=t[4]^t[1]>>>16^t[7]<<16,c[3]=t[6]^t[3]>>>16^t[1]<<16;for(var o=0;o<4;o++)c[o]=16711935&(c[o]<<8|c[o]>>>24)|4278255360&(c[o]<<24|c[o]>>>8),e[i+o]^=c[o]},blockSize:4,ivSize:2});i.Rabbit=o._createHelper(n)}(),e.Rabbit});