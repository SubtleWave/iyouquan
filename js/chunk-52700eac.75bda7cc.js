(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-52700eac"],{"63e6":function(t,e,a){"use strict";var s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"design-resource"},[t.$slots.label?a("div",{staticClass:"design-resource__label"},[t._t("label")],2):t._e(),a("div",{staticClass:"design-resource__content"},[t._t("default")],2)])},n=[],i={name:"vx-resource"},l=i,o=a("2877"),r=Object(o["a"])(l,s,n,!1,null,null,null);e["a"]=r.exports},d751:function(t,e,a){"use strict";a.r(e);var s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("vx-template",{staticClass:"design-resource-element",scopedSlots:t._u([{key:"label",fn:function(){return[a("h2",[t._v("图形")])]},proxy:!0}])},[a("div",{staticClass:"design-resource-scene"},[a("div",{staticClass:"design-resource-element-list"},[a("ul",[t._l(t.templateList,(function(e,s){return a("li",{key:s,on:{click:function(a){return t.selectHandler(e.id)}}},[a("el-image",{staticClass:"design-resource-element-item",attrs:{fit:"contain",src:e.typefacePath}},[a("div",{staticClass:"image-slot",attrs:{slot:"placeholder"},slot:"placeholder"}),a("div",{staticClass:"image-slot",attrs:{slot:"error"},slot:"error"},[a("i",{staticClass:"el-icon-picture-outline"})])])],1)})),a("div",{staticClass:"load-bottom",attrs:{id:"element-bottom"}})],2)]),t.loading?a("VxLoading"):t._e(),t.noMore?a("p",{staticClass:"nomore-text"},[t._v("已全部加载完")]):t._e()],1)])},n=[],i=(a("99af"),a("96cf"),a("1da1")),l=a("63e6"),o=a("81ac"),r=a("a10b"),c={name:"vx-element-scene",components:{VxTemplate:l["a"]},data:function(){return{templateList:[],loading:!0,pageNum:1,noMore:!1}},computed:{},mounted:function(){var t=this;return Object(i["a"])(regeneratorRuntime.mark((function e(){return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,t.getDataHandler();case 2:t.scrollData();case 3:case"end":return e.stop()}}),e)})))()},methods:{scrollData:function(){var t=this,e=new IntersectionObserver((function(e){var a=e[0];a.isIntersecting&&(t.noMore||t.getDataHandler())}));e.observe(document.getElementById("element-bottom"))},getDataHandler:function(){var t=this;this.loading=!0;var e={page:this.pageNum,limit:21,type:"img",l:0,t:0};this.$axios.get(r["a"].EFFECTS_API,{params:e}).then((function(e){t.totalPages=e.data.pages,t.templateList.length>=e.data.total&&(t.noMore=!0),t.loading=!1,t.templateList=t.templateList.concat(e.data.list),t.pageNum++})).catch((function(t){r["c"].danger(t)}))},selectHandler:function(t){var e=this,a={id:t};this.setTextPositionHandler(),this.$axios.get(r["a"].EFFECT_DETAIL_API,{params:a}).then((function(t){console.log(t);var a=JSON.parse(t.data.info.detail);t.left=e.l,t.top=e.t,o["b"].addImage(a.model),e.$emit("select")})).catch((function(t){r["c"].danger(t)}))},setTextPositionHandler:function(){o["a"].canvasH()-80<this.t&&(this.l+=40,this.t=0),this.t+=40,this.l+=40,o["a"].canvasW()-80<this.l&&(this.t=0,this.l=0)}}},u=c,d=a("2877"),m=Object(d["a"])(u,s,n,!1,null,null,null),g=m.exports;e["default"]=g}}]);