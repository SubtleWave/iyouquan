(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d21a92d"],{bba8:function(t,a,i){"use strict";i.r(a);var s=function(){var t=this,a=t.$createElement,i=t._self._c||a;return i("div",{staticClass:"vx-bannerDiv"},[i("div",[i("div",{staticClass:"vx-tags vx-search-page__tags"},[i("div",{staticClass:"vx-tags-menu"},[i("div",{staticClass:"left"},[t._v("类型")]),i("div",{staticClass:"vx-tags__group"},[i("div",{staticClass:"vx-tags__item",class:null==t.activeClassFirst?"activeFirst":"",on:{click:function(a){return t.allFirst()}}},[t._v(" 全部 ")]),t._l(t.newFirstData,(function(a,s){return i("div",{key:s,staticClass:"vx-tags__item",class:t.activeClassFirst==a.id?"activeFirst":"",on:{click:function(i){return t.getFirstList(a.id)}}},[t._v(" "+t._s(t.newFirstData[s].name)+" ")])}))],2)]),i("div",{staticClass:"vx-tags-menu"},[i("div",{staticClass:"left",staticStyle:{width:"4rem"}},[t._v("分类")]),i("div",{staticClass:"vx-tags__group"},[i("div",{ref:"secondType",staticClass:"vx-search-first-item"},t._l(t.newSecondData,(function(a,s){return i("li",{key:s,staticClass:"vx-tags__item",class:t.activeClass==a.id?"active":"",on:{click:function(i){t.activeClass!=a.id&&t.getItem(a.id)}}},[t._v(" "+t._s(t.newSecondData[s].name)+" ")])})),0)])])])])])},e=[],n=i("4ec3"),c={name:"vx-ModelBanner",props:{activesSearch:Boolean,categoryListProps:Array},data:function(){return{msg:"Welcome to Your Vue.js App",activeClass:0,activeClassFirst:null,categoryListFirst:[],categoryList:[],level2ListId:[],level2ListFirstId:0,allTitList:{},newFirstData:[],newSecondData:[],allSecondData:[],secondRef:""}},mounted:function(){this.initData()},methods:{initData:function(){var t=this;Object(n["e"])().then((function(a){for(var i in t.allTitList=a.data.allList,t.allTitList){var s=t.allTitList[i];for(var e in s)t.allSecondData.push(s[e])}t.newFirstData=a.data.categoryList,t.newSecondData=t.allSecondData}))},allFirst:function(){var t=this;this.activeClassFirst=null,this.activeClass=null,this.newSecondData=[],Object(n["e"])().then((function(a){for(var i in t.allTitList=a.data.allList,t.allTitList){var s=t.allTitList[i];for(var e in s)t.newSecondData.push(s[e])}}));var a={type:1};this.$emit("selectCatalog",a)},getFirstList:function(t){this.activeClassFirst=t,this.newSecondData=this.allTitList[t],this.activeClass=this.newSecondData[0].id;var a={id:this.newSecondData[0].id,type:1};this.$emit("selectCatalog",a)},getItem:function(t){this.activeClass=t;var a={id:t,type:1};this.$emit("selectCatalog",a)}},watch:{categoryListProps:function(){this.categoryList=this.categoryListProps}},computed:{allSearch:function(){return 1==this.activesSearch}},created:function(){var t=this;Object(n["g"])().then((function(a){t.level2ListFirstId=a.data.categoryList[0].id,t.categoryListFirst.length=0,t.categoryListFirst=a.data.categoryList}))}},l=c,r=i("2877"),o=Object(r["a"])(l,s,e,!1,null,null,null);a["default"]=o.exports}}]);