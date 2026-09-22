<template>
  <el-card>
    <template #header>库存调整记录</template>
    <el-table :data="adjustments" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column label="批次" width="170">
        <template #default="{ row }">{{ row.batch.batchNo }}</template>
      </el-table-column>
      <el-table-column label="产品" min-width="150">
        <template #default="{ row }">{{ row.batch.product.name }}</template>
      </el-table-column>
      <el-table-column label="原因" width="180">
        <template #default="{ row }">{{ row.reason.name }}</template>
      </el-table-column>
      <el-table-column label="旧 → 新" width="130">
        <template #default="{ row }">{{ row.oldQuantity }} → {{ row.newQuantity }}</template>
      </el-table-column>
      <el-table-column label="差值" width="90">
        <template #default="{ row }">
          <span :class="row.quantityDiff < 0 ? 'neg' : 'pos'">{{ row.quantityDiff }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" min-width="150" show-overflow-tooltip>
        <template #default="{ row }">{{ row.note || '-' }}</template>
      </el-table-column>
      <el-table-column prop="adjustedBy" label="操作人" width="100" />
      <el-table-column label="时间" width="170">
        <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="90" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="goDetail(row.id)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { fetchAdjustments } from '@/api'
import type { InventoryAdjustmentView } from '@/types'

const router = useRouter()
const adjustments = ref<InventoryAdjustmentView[]>([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    adjustments.value = await fetchAdjustments()
  } catch {
    // 错误提示已由 http 拦截器统一处理
  } finally {
    loading.value = false
  }
})

function goDetail(id: number) {
  router.push(`/adjustments/${id}`)
}

function formatTime(value: string) {
  return value.replace('T', ' ')
}
</script>

<style scoped>
.neg {
  color: #f56c6c;
  font-weight: 600;
}
.pos {
  color: #67c23a;
  font-weight: 600;
}
</style>
