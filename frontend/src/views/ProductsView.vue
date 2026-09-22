<template>
  <el-card>
    <template #header>产品列表</template>
    <el-table :data="products" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="sku" label="SKU" width="180" />
      <el-table-column prop="name" label="产品名称" />
      <el-table-column prop="unit" label="单位" width="120" />
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { fetchProducts } from '@/api'
import type { Product } from '@/types'

const products = ref<Product[]>([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    products.value = await fetchProducts()
  } catch {
    // 错误提示已由 http 拦截器统一处理
  } finally {
    loading.value = false
  }
})
</script>
