<template>
  <el-card>
    <template #header>
      <div class="inventory-header">
        <span>产品库存（按批次）</span>
        <div>
          <el-button type="primary" @click="$router.push('/adjustments/new')">单次调整</el-button>
          <el-button type="warning" @click="$router.push('/adjustments/batch')">批量调整</el-button>
        </div>
      </div>
    </template>
    <el-table :data="batches" v-loading="loading" border stripe>
      <el-table-column prop="batchNo" label="批次号" width="180" />
      <el-table-column prop="productName" label="产品" min-width="160" />
      <el-table-column prop="productSku" label="SKU" width="140" />
      <el-table-column prop="warehouseName" label="仓库" width="160" />
      <el-table-column prop="quantity" label="当前数量" width="120" />
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { fetchBatches } from '@/api'
import type { BatchView } from '@/types'

const batches = ref<BatchView[]>([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    batches.value = await fetchBatches()
  } catch {
    // 错误提示已由 http 拦截器统一处理
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.inventory-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
