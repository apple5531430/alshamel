<template>
  <el-card>
    <template #header>
      <div class="batch-header">
        <span>批量库存调整</span>
        <el-button type="primary" plain @click="addRow">+ 添加一行</el-button>
      </div>
    </template>

    <div v-for="(row, index) in rows" :key="index" class="batch-row">
      <div class="row-index">#{{ index + 1 }}</div>
      <el-select
        v-model="row.batchId"
        placeholder="选择批次"
        style="width: 220px"
        @change="(v: number) => onBatchChange(index, v)"
      >
        <el-option
          v-for="b in batchOptions(index)"
          :key="b.id"
          :value="b.id"
          :label="`${b.batchNo}（${b.productName}）`"
        />
      </el-select>
      <el-select v-model="row.reasonId" placeholder="选择原因" style="width: 190px">
        <el-option v-for="r in reasons" :key="r.id" :value="r.id" :label="r.name" />
      </el-select>
      <el-input-number v-model="row.newQuantity" :min="0" style="width: 140px" />
      <el-input v-model="row.note" placeholder="备注（可选）" style="width: 180px" />
      <el-button link type="danger" @click="removeRow(index)">删除</el-button>
    </div>

    <div class="batch-actions">
      <el-button type="primary" :loading="submitting" @click="onSubmit">提交批量调整</el-button>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createBatchAdjustments, fetchBatches, fetchReasons } from '@/api'
import type { AdjustmentReason, BatchView } from '@/types'

interface Row {
  batchId: number | undefined
  reasonId: number | undefined
  newQuantity: number | undefined
  note: string
}

const router = useRouter()
const reasons = ref<AdjustmentReason[]>([])
const batches = ref<BatchView[]>([])
const submitting = ref(false)
const rows = ref<Row[]>([emptyRow()])

function emptyRow(): Row {
  return { batchId: undefined, reasonId: undefined, newQuantity: undefined, note: '' }
}

onMounted(async () => {
  try {
    const [r, b] = await Promise.all([fetchReasons(), fetchBatches()])
    reasons.value = r
    batches.value = b
  } catch {
    // 错误提示已由 http 拦截器统一处理
  }
})

function addRow() {
  rows.value.push(emptyRow())
}

function removeRow(index: number) {
  rows.value.splice(index, 1)
  if (rows.value.length === 0) {
    rows.value.push(emptyRow())
  }
}

// 每行只展示未被其它行选中的批次，避免重复批次
function batchOptions(index: number) {
  const selectedElsewhere = rows.value
    .filter((_, i) => i !== index)
    .map((r) => r.batchId)
    .filter((id): id is number => id != null)
  return batches.value.filter((b) => !selectedElsewhere.includes(b.id))
}

function onBatchChange(index: number, batchId: number) {
  const batch = batches.value.find((b) => b.id === batchId)
  if (batch) {
    rows.value[index].newQuantity = batch.quantity
  }
}

async function onSubmit() {
  for (let i = 0; i < rows.value.length; i++) {
    const r = rows.value[i]
    if (r.batchId == null) {
      ElMessage.warning(`第 ${i + 1} 行：请选择批次`)
      return
    }
    if (r.reasonId == null) {
      ElMessage.warning(`第 ${i + 1} 行：请选择原因`)
      return
    }
    if (r.newQuantity == null) {
      ElMessage.warning(`第 ${i + 1} 行：请输入新数量`)
      return
    }
  }
  submitting.value = true
  try {
    await createBatchAdjustments({
      items: rows.value.map((r) => ({
        batchId: r.batchId!,
        reasonId: r.reasonId!,
        newQuantity: r.newQuantity!,
        note: r.note || undefined
      }))
    })
    ElMessage.success(`成功创建 ${rows.value.length} 条调整`)
    router.push('/adjustments')
  } catch {
    // 错误提示已由 http 拦截器统一处理
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.batch-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.batch-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 14px;
}
.row-index {
  width: 36px;
  color: #909399;
  flex-shrink: 0;
}
.batch-actions {
  margin-top: 8px;
}
</style>
