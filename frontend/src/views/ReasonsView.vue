<template>
  <el-card>
    <template #header>预定义调整原因</template>

    <el-table :data="reasons" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="code" label="编码" width="200" />
      <el-table-column prop="name" label="名称" />
      <el-table-column label="启用" width="100">
        <template #default="{ row }">
          <el-switch :model-value="row.active" @change="(val: boolean) => onToggle(row, val)" />
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { fetchAllReasons, updateReasonActive } from '@/api'
import type { AdjustmentReason } from '@/types'

const reasons = ref<AdjustmentReason[]>([])
const loading = ref(false)

async function load() {
  loading.value = true
  try {
    reasons.value = await fetchAllReasons()
  } catch {
    // 错误提示已由 http 拦截器统一处理
  } finally {
    loading.value = false
  }
}

onMounted(load)

async function onToggle(row: AdjustmentReason, val: boolean) {
  try {
    await updateReasonActive(row.id, val)
    row.active = val
  } catch {
    // 失败不更新本地状态，switch 会回弹
  }
}
</script>
