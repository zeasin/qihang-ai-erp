<template>
  <div class="basic-layout" style="height:100%;display:flex;flex-direction:column">
    <el-container style="height:100%">
      <el-header style="padding:0 16px;display:flex;align-items:center;border-bottom:1px solid #e0e0e0;background:#fff;height:48px;flex-shrink:0">
        <el-button text @click="$router.push('/')" size="small">← 首页</el-button>
        <span style="font-size:16px;font-weight:600;margin-left:12px">采购管理</span>
      </el-header>
      <el-container style="flex:1;overflow:hidden">
        <el-aside width="200px" style="border-right:1px solid #e0e0e0;background:#fafafa">
          <el-menu :default-active="$route.path" router style="border-right:none">
            <el-menu-item index="/purchase/list">采购订单</el-menu-item>
            <el-menu-item index="/purchase/stock-in">采购入库</el-menu-item>
          </el-menu>
        </el-aside>
        <el-main style="padding:16px;overflow:auto;background:#f5f7fa">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>