(window.webpackJsonp=window.webpackJsonp||[]).push([["oiju"],{oiju:function(t,e,l){"use strict";l.r(e);var i=l("t3Un");var n=l("+Lc1"),a={name:"Comment",components:{Pagination:l("Mz3J").a},data:function(){return{list:[],total:0,listLoading:!0,listQuery:{page:1,limit:20,userId:void 0,valueId:void 0,sort:"add_time",order:"desc"},downloadLoading:!1,replyForm:{commentId:0,content:""},replyFormVisible:!1}},created:function(){this.getList()},methods:{getList:function(){var t=this;this.listLoading=!0,function(t){return Object(i.a)({url:"/comment/list",method:"get",params:t})}(this.listQuery).then(function(e){t.list=e.data.data.list,t.total=e.data.data.total,t.listLoading=!1}).catch(function(){t.list=[],t.total=0,t.listLoading=!1})},handleFilter:function(){this.listQuery.page=1,this.getList()},handleReply:function(t){this.replyForm={commentId:t.id,content:""},this.replyFormVisible=!0},reply:function(){var t=this;Object(n.d)(this.replyForm).then(function(e){t.replyFormVisible=!1,t.$notify.success({title:"成功",message:"回复成功"})}).catch(function(e){t.$notify.error({title:"失败",message:e.data.errmsg})})},handleDelete:function(t){var e=this;(function(t){return Object(i.a)({url:"/comment/delete",method:"post",data:t})})(t).then(function(l){e.$notify({title:"成功",message:"删除成功",type:"success",duration:2e3});var i=e.list.indexOf(t);e.list.splice(i,1)})},handleDownload:function(){var t=this;this.downloadLoading=!0,Promise.all([l.e("chunk-0d49"),l.e("chunk-eb6b")]).then(l.bind(null,"S/jZ")).then(function(e){e.export_json_to_excel2(["评论ID","用户ID","商品ID","评论","评论图片列表","评论时间"],t.list,["id","userId","valueId","content","picUrls","addTime"],"商品评论信息"),t.downloadLoading=!1})}}},o=l("KHd+"),r=Object(o.a)(a,function(){var t=this,e=t.$createElement,l=t._self._c||e;return l("div",{staticClass:"app-container"},[l("div",{staticClass:"filter-container"},[l("el-input",{staticClass:"filter-item",staticStyle:{width:"200px"},attrs:{clearable:"",placeholder:"请输入用户ID"},model:{value:t.listQuery.userId,callback:function(e){t.$set(t.listQuery,"userId",e)},expression:"listQuery.userId"}}),t._v(" "),l("el-input",{staticClass:"filter-item",staticStyle:{width:"200px"},attrs:{clearable:"",placeholder:"请输入商品ID"},model:{value:t.listQuery.valueId,callback:function(e){t.$set(t.listQuery,"valueId",e)},expression:"listQuery.valueId"}}),t._v(" "),l("el-button",{staticClass:"filter-item",attrs:{type:"primary",icon:"el-icon-search"},on:{click:t.handleFilter}},[t._v("查找")]),t._v(" "),l("el-button",{staticClass:"filter-item",attrs:{loading:t.downloadLoading,type:"primary",icon:"el-icon-download"},on:{click:t.handleDownload}},[t._v("导出")])],1),t._v(" "),l("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.listLoading,expression:"listLoading"}],attrs:{data:t.list,"element-loading-text":"正在查询中。。。",border:"",fit:"","highlight-current-row":""}},[l("el-table-column",{attrs:{align:"center",label:"用户ID",prop:"userId"}}),t._v(" "),l("el-table-column",{attrs:{align:"center",label:"商品ID",prop:"valueId"}}),t._v(" "),l("el-table-column",{attrs:{align:"center",label:"打分",prop:"star"}}),t._v(" "),l("el-table-column",{attrs:{align:"center",label:"评论内容",prop:"content"}}),t._v(" "),l("el-table-column",{attrs:{align:"center",label:"评论图片",prop:"picUrls"},scopedSlots:t._u([{key:"default",fn:function(e){return t._l(e.row.picUrls,function(t){return l("img",{key:t,attrs:{src:t,width:"40"}})})}}])}),t._v(" "),l("el-table-column",{attrs:{align:"center",label:"时间",prop:"addTime"}}),t._v(" "),l("el-table-column",{attrs:{align:"center",label:"操作",width:"200","class-name":"small-padding fixed-width"},scopedSlots:t._u([{key:"default",fn:function(e){return[l("el-button",{attrs:{type:"primary",size:"mini"},on:{click:function(l){t.handleReply(e.row)}}},[t._v("回复")]),t._v(" "),l("el-button",{attrs:{type:"danger",size:"mini"},on:{click:function(l){t.handleDelete(e.row)}}},[t._v("删除")])]}}])})],1),t._v(" "),l("pagination",{directives:[{name:"show",rawName:"v-show",value:t.total>0,expression:"total>0"}],attrs:{total:t.total,page:t.listQuery.page,limit:t.listQuery.limit},on:{"update:page":function(e){t.$set(t.listQuery,"page",e)},"update:limit":function(e){t.$set(t.listQuery,"limit",e)},pagination:t.getList}}),t._v(" "),l("el-dialog",{attrs:{visible:t.replyFormVisible,title:"回复"},on:{"update:visible":function(e){t.replyFormVisible=e}}},[l("el-form",{ref:"replyForm",staticStyle:{width:"400px","margin-left":"50px"},attrs:{model:t.replyForm,"status-icon":"","label-position":"left","label-width":"100px"}},[l("el-form-item",{attrs:{label:"回复内容",prop:"content"}},[l("el-input",{attrs:{autosize:{minRows:4,maxRows:8},type:"textarea"},model:{value:t.replyForm.content,callback:function(e){t.$set(t.replyForm,"content",e)},expression:"replyForm.content"}})],1)],1),t._v(" "),l("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[l("el-button",{on:{click:function(e){t.replyFormVisible=!1}}},[t._v("取消")]),t._v(" "),l("el-button",{attrs:{type:"primary"},on:{click:t.reply}},[t._v("确定")])],1)],1)],1)},[],!1,null,null,null);r.options.__file="comment.vue";e.default=r.exports}}]);