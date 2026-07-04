<template>
  <div class="page-card">
    <div class="page-header"><h3>出库单列表</h3></div>
    <el-form :model="query" inline size="small" style="margin-bottom:12px">
      <el-form-item label="出库单号"><el-input v-model="query.outNum" placeholder="搜索" clearable style="width:160px" @keyup.enter="search" /></el-form-item>
      <el-form-item label="来源单号"><el-input v-model="query.sourceNum" placeholder="搜索" clearable style="width:160px" @keyup.enter="search" /></el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" placeholder="全部" clearable style="width:120px">
          <el-option label="已出库" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" @click="search">查询</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
    </el-form>
    <el-table :data="list" stripe border size="small" v-loading="loading">
      <el-table-column prop="outNum" label="出库单号" width="180" />
      <el-table-column prop="sourceNum" label="来源订单" width="160" />
      <el-table-column prop="warehouseName" label="仓库" width="120" />
      <el-table-column prop="specUnitTotal" label="总件数" width="70" align="center" />
      <el-table-column label="状态" width="80" align="center">
        <template #default="{ row }"><el-tag :type="row.status===2?'success':'warning'" size="small">{{ row.status===2?'已出库':row.status===1?'部分出库':'待出库' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="80" align="center" fixed="right">
        <template #default="{ row }"><el-button size="small" type="primary" link @click="showDetail(row)">详情</el-button></template>
      </el-table-column>
    </el-table>
    <div style="margin-top:12px;text-align:right"><el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" @change="fetchData" /></div>
    <el-dialog v-model="detailVisible" title="出库单详情" width="700px">
      <template v-if="detail">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="出库单号" :span="2">{{ detail.delivery.outNum }}</el-descriptions-item>
          <el-descriptions-item label="来源订单">{{ detail.delivery.sourceNum }}</el-descriptions-item>
          <el-descriptions-item label="仓库">{{ detail.delivery.warehouseName }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detail.delivery.remark }}</el-descriptions-item>
        </el-descriptions>
        <el-divider /><h4 style="margin:0 0 8px">出库商品</h4>
        <el-table :data="detail.itemList||[]" border size="small">
          <el-table-column prop="goodsName" label="商品" min-width="180" />
          <el-table-column prop="skuName" label="规格" width="120" />
          <el-table-column prop="skuCode" label="编码" width="100" />
          <el-table-column prop="originalQuantity" label="数量" width="60" align="center" />
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
const query=reactive({outNum:'',sourceNum:'',status:undefined as any})
const detailVisible=ref(false);const detail=ref<any>(null)
function search(){pageNum.value=1;fetchData()}
function resetQuery(){Object.assign(query,{outNum:'',sourceNum:'',status:undefined});search()}
async function fetchData(){loading.value=true;try{const res:any=await request.get(api.deliveryList,{params:{pageNum:pageNum.value,pageSize:pageSize.value,...query}});list.value=res.rows||[];total.value=res.total||0}finally{loading.value=false}}
async function showDetail(row:any){const res:any=await request.get(api.deliveryGet(row.id));detail.value=res.data;detailVisible.value=true}
onMounted(fetchData)
</script>