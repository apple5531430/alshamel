<template>
  <el-card v-loading="loading">
    <template #header>
      <div class="detail-header">
        <span>调整详情 #{{ id }}</span>
        <el-button link @click="$router.back()">返回</el-button>
      </div>
    </template>

    <el-descriptions v-if="detail" :column="2" border>
      <el-descriptions-item label="调整 ID">{{ detail.id }}</el-descriptions-item>
      <el-descriptions-item label="操作人">{{ detail.adjustedBy }}</el-descriptions-item>
      <el-descriptions-item label="旧数量">{{ detail.oldQuantity }}</el-descriptions-item>
      <el-descriptions-item label="新数量">{{ detail.newQuantity }}</el-descriptions-item>
      <el-descriptions-item label="差值">
        <span :class="detail.quantityDiff < 0 ? 'diff-negative' : 'diff-positive'">
          {{ detail.quantityDiff }}
        </span>
      </el-descriptions-item>
      <el-descriptions-item label="调整原因">
        {{ detail.reason.name }}（{{ detail.reason.code }}）
      </el-descriptions-item>
      <el-descriptions-item label="批次号">{{ detail.batch.batchNo }}</el-descriptions-item>
      <el-descriptions-item label="产品">
        {{ detail.batch.product.name }}（{{ detail.batch.product.sku }}）
      </el-descriptions-item>
      <el-descriptions-item label="仓库">
        {{ detail.batch.warehouse.name }}（{{ detail.batch.warehouse.code }}）
      </el-descriptions-item>
      <el-descriptions-item label="批次当前数量">{{ detail.batch.quantity }}</el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ formatTime(detail.createdAt) }}</el-descriptions-item>
      <el-descriptions-item label="备注" :span="2">{{ detail.note || '-' }}</el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { fetchAdjustment } from '@/api'
import type { InventoryAdjustmentView } from '@/types'

const route = useRoute()
const id = route.params.id as string
const detail = ref<InventoryAdjustmentView | null>(null)
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    detail.value = await fetchAdjustment(Number(id))
  } catch {
    // 错误提示已由 http 拦截器统一处理
  } finally {
    loading.value = false
  }
})

function formatTime(value: string) {
  return value.replace('T', ' ')
}
</script>

<style scoped>
.detail-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.diff-negative {
  color: #f56c6c;
  font-weight: 600;
}
.diff-positive {
  color: #67c23a;
  font-weight: 600;
}
</style>
