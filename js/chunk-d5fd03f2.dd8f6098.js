(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-d5fd03f2","chunk-9d3f7c66"],{"40ca":function(t,e,n){},"919d":function(t,e,n){"use strict";var i=n("40ca"),a=n.n(i);a.a},a8fb:function(t,e,n){"use strict";n.r(e);var i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"home-head"},[t._m(0),n("div",{staticClass:"vx-nav"},[n("li",{class:"/"==t.$route.path?"active":"",on:{click:function(e){return t.$router.push("/")}}},[n("div",{staticClass:"border"}),t._v("首页 ")]),n("li",{class:"/mall/modelCenter"==t.$route.path?"active":"",on:{click:function(e){return t.$router.push("/mall/modelCenter")}}},[n("div",{staticClass:"border"}),t._v("模板中心 ")]),n("li",{class:"/miniProgram"==t.$route.path?"active":"",on:{click:function(e){return t.$router.push("/miniProgram")}}},[n("div",{staticClass:"border"}),t._v("移动端 ")])]),t.isLogin?t._e():n("div",{staticClass:"login",on:{click:function(e){return t.$store.commit("updateShowLogin",!0)}}},[t._v("登录")]),t.isLogin?n("div",{staticClass:"afterlogin"},[t.isLogin?n("el-row",{staticClass:"block-col-2"},[n("el-col",{attrs:{span:8}},[n("el-dropdown",{staticStyle:{width:"150px",padding:"16px"},attrs:{trigger:"click"}},[n("span",{staticClass:"el-dropdown-link"},[n("el-avatar",{attrs:{size:35,src:t.avatarUrl}}),n("div",{staticStyle:{"padding-left":"5px"}},[t._v(t._s(t.userName))]),n("i",{staticClass:"el-icon-arrow-down el-icon--right"})],1),n("el-dropdown-menu",{staticClass:"vx-mall-header-dropdown",attrs:{slot:"dropdown"},slot:"dropdown"},["/"!=t.$route.path?n("el-dropdown-item",{nativeOn:{click:function(e){return t.$router.push("/")}}},[n("vx-icon",{attrs:{name:"home"}}),t._v("首页 ")],1):t._e(),n("el-dropdown-item",{nativeOn:{click:function(e){return t.$router.push("/user/myworks")}}},[n("vx-icon",{attrs:{name:"opus"}}),t._v("我的作品 ")],1),n("el-dropdown-item",{nativeOn:{click:function(e){return t.$router.push("/user/myCollect")}}},[n("vx-icon",{attrs:{name:"collection"}}),t._v("我的收藏 ")],1),n("el-dropdown-item",{nativeOn:{click:function(e){return t.exitUser(e)}}},[n("vx-icon",{attrs:{name:"logout"}}),t._v("退出登录 ")],1)],1)],1)],1)],1):t._e()],1):t._e()])},a=[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"logo"},[n("div",{staticClass:"logo-img"}),n("div",{staticClass:"logo-text"},[t._v("爱友圈")])])}],s=n("6b43"),o={name:"vx-head",computed:{isLogin:function(){return this.$store.getters.getLoginState()},userName:function(){return s["a"].getItem("userName")},avatarUrl:function(){return s["a"].getItem("avatarUrl")}},data:function(){return{showFixed:!1}},mounted:function(){},methods:{exitUser:function(){this.$store.commit("updateShowLogin",!1),this.$store.commit("updateLoginState",!1),s["a"].clear()}}},r=o,c=(n("919d"),n("2877")),l=Object(c["a"])(r,i,a,!1,null,"4197a3d6",null);e["default"]=l.exports},efe1:function(t,e,n){"use strict";n.r(e);var i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"vx-home"},[n("VxHead"),n("div",{staticClass:"home-content"},[n("div",{staticClass:"home-image"}),t._m(0),n("div",{staticClass:"vx-interduce"},[t._v("极简在线编辑器，丰富模板，多终端协作极简在线编辑器，丰富模板，多终端协作！")]),n("div",{staticClass:"vx-begin",on:{click:function(e){return t.bieginDesign("/design/new")}}},[t._v("开始制作")]),n("div",{staticClass:"copyright"},[t._v("© 2020 iyouquan.cn 版权所有")])])],1)},a=[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"vx-title"},[t._v(" 快速作图， "),n("br"),t._v("随时随地分享 ")])}],s=n("a8fb"),o={name:"home",components:{VxHead:s["default"]},methods:{bieginDesign:function(t){var e=this.$router.resolve({path:t}),n=e.href;window.open(n,"_blank")}}},r=o,c=n("2877"),l=Object(c["a"])(r,i,a,!1,null,null,null);e["default"]=l.exports}}]);