<template>
  <div class="page-card">
    <div class="page-header"><h3>入库单列表</h3></div>
    <el-form :model="query" inline size="small" style="margin-bottom:12px">
      <el-form-item label="入库单号"><el-input v-model="query.inNum" placeholder="搜索" clearable style="width:160px" @keyup.enter="search" /></el-form-item>
      <el-form-item label="来源单号"><el-input v-model="query.sourceNo" placeholder="搜索" clearable style="width:160px" @keyup.enter="search" /></el-form-item>
      <el-form-item><el-button type="primary" @click="search">查询</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
    </el-form>
    <el-table :data="list" stripe border size="small" v-loading="loading">
      <el-table-column prop="inNum" label="入库单号" width="180" />
      <el-table-column prop="sourceNo" label="来源单号" width="160" />
      <el-table-column prop="warehouseName" label="仓库" width="120" />
      <el-table-column prop="inTotal" label="入库总数" width="70" align="center" />
      <el-table-column label="类型" width="80" align="center">
        <template #default="{ row }">{{ row.type === 2 ? '采购入库' : row.type === 4 ? '退货入库' : '其他' }}</template>
      </el-table-column>
      <el-table-column label="状态" width="80" align="center">
        <template #default="{ row }"><el-tag :type="row.status===2?'success':'warning'" size="small">{{ row.status===2?'已完成':'待处理' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="入库时间" width="150">
        <template #default="{ row }">{{ row.inTime ? (row.inTime + '').split('.').slice(0,1).join('').replace('T',' ') : '-' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="80" align="center" fixed="right">
        <template #default="{ row }"><el-button size="small" type="primary" link @click="showDetail(row)">详情</el-button></template>
      </el-table-column>
    </el-table>
    <div style="margin-top:12px;text-align:right"><el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" @change="fetchData" /></div>
    <el-dialog v-model="detailVisible" title="入库单详情" width="700px">
      <template v-if="detail">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="入库单号" :span="2">{{ detail.inNum }}</el-descriptions-item>
          <el-descriptions-item label="来源单号">{{ detail.sourceNo }}</el-descriptions-item>
          <el-descriptions-item label="仓库">{{ detail.warehouseName }}</el-descriptions-item>
          <el-descriptions-item label="入库总数">{{ detail.inTotal }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detail.remark }}</el-descriptions-item>
        </el-descriptions>
        <el-divider /><h4 style="margin:0 0 8px">入库商品</h4>
        <el-table :data="detail.itemList||[]" border size="small">
          <el-table-column prop="goodsName" label="商品" min-width="180" />
          <el-table-column prop="skuName" label="规格" width="120" />
          <el-table-column prop="skuCode" label="编码" width="100" />
          <el-table-column prop="quantity" label="数量" width="60" align="center" />
          <el-table-column prop="inQuantity" label="入库数" width="60" align="center" />
        </el-table>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import request from '../../utils/request'
import { api } from '../../utils/api'
const loading=ref(false);const list=ref<any[]>([]);const total=ref(0);const pageNum=ref(1);const pageSize=ref(20)
const query=reactive({inNum:'',sourceNo:''})
const detailVisible=ref(false);const detail=ref<any>(null)
function search(){pageNum.value=1;fetchData()}
function resetQuery(){Object.assign(query,{inNum:'',sourceNo:''});search()}
async function fetchData(){loading.value=true;try{const res:any=await request.get(api.stockInboundList,{params:{pageNum:pageNum.value,pageSize:pageSize.value,...query}});list.value=res.rows||[];total.value=res.total||0}finally{loading.value=false}}
async function showDetail(row:any){const res:any=await request.get(api.stockInboundGet(row.id));detail.value=res.data;detailVisible.value=true}
onMounted(fetchData)
</script>