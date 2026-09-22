<template>
  <el-container class="layout">
    <el-header class="layout-header">
      <div class="brand">Alshamel 库存调整</div>
      <div class="header-right">
        <span class="username">{{ auth.username }}</span>
        <el-button link type="primary" @click="onLogout">退出登录</el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px" class="layout-aside">
        <el-menu :default-active="activeMenu" router>
          <el-menu-item index="/products">产品</el-menu-item>
          <el-menu-item index="/inventory">库存</el-menu-item>
          <el-menu-item index="/reasons">调整原因</el-menu-item>
          <el-menu-item index="/adjustments">调整记录</el-menu-item>
          <el-menu-item index="/adjustments/new">单次调整</el-menu-item>
          <el-menu-item index="/adjustments/batch">批量调整</el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const activeMenu = computed(() => route.path)

function onLogout() {
  auth.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  height: 100%;
}
.layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
}
.brand {
  font-size: 18px;
  font-weight: 600;
  color: #409eff;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}
.username {
  color: #606266;
}
.layout-aside {
  background: #fff;
  border-right: 1px solid #e4e7ed;
}
</style>
