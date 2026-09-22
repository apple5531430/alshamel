<template>
  <el-card>
    <template #header>新建库存调整</template>

    <el-form label-width="110px" style="max-width: 620px">
      <el-form-item label="批次">
        <el-select
          v-model="form.batchId"
          placeholder="请选择批次"
          style="width: 100%"
          @change="onBatchChange"
        >
          <el-option
            v-for="b in batches"
            :key="b.id"
            :value="b.id"
            :label="`${b.batchNo}（${b.productName} · 当前 ${b.quantity}）`"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="调整原因">
        <el-select v-model="form.reasonId" placeholder="请选择原因" style="width: 100%">
          <el-option v-for="r in reasons" :key="r.id" :value="r.id" :label="r.name" />
        </el-select>
      </el-form-item>

      <el-form-item label="新数量">
        <el-input-number v-model="form.newQuantity" :min="0" style="width: 100%" />
      </el-form-item>

      <el-form-item label="备注">
        <el-input
          v-model="form.note"
          type="textarea"
          :rows="3"
          maxlength="500"
          show-word-limit
          placeholder="可选"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" :loading="submitting" @click="onSubmit">提交调整</el-button>
      </el-form-item>
    </el-form>

    <el-alert
      v-if="result"
      type="success"
      :closable="false"
      style="margin-top: 16px"
    >
      <template #title>
        调整成功！旧数量 {{ result.oldQuantity }} → 新数量 {{ result.newQuantity }}（差值 {{ result.quantityDiff }}）
      </template>
      <el-button link type="primary" @click="goDetail">查看详情 #{{ result.id }}</el-button>
    </el-alert>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createAdjustment, fetchBatches, fetchReasons } from '@/api'
import type { AdjustmentReason, BatchView, InventoryAdjustmentView } from '@/types'

const router = useRouter()
const reasons = ref<AdjustmentReason[]>([])
const batches = ref<BatchView[]>([])
const submitting = ref(false)
const result = ref<InventoryAdjustmentView | null>(null)

const form = reactive({
  batchId: undefined as number | undefined,
  reasonId: undefined as number | undefined,
  newQuantity: undefined as number | undefined,
  note: ''
})

onMounted(async () => {
  try {
    const [r, b] = await Promise.all([fetchReasons(), fetchBatches()])
    reasons.value = r
    batches.value = b
  } catch {
    // 错误提示已由 http 拦截器统一处理
  }
})

function onBatchChange(batchId: number) {
  const batch = batches.value.find((b) => b.id === batchId)
  if (batch) {
    form.newQuantity = batch.quantity
  }
}

async function onSubmit() {
  if (form.batchId == null) {
    ElMessage.warning('请选择批次')
    return
  }
  if (form.reasonId == null) {
    ElMessage.warning('请选择调整原因')
    return
  }
  if (form.newQuantity == null) {
    ElMessage.warning('请输入新数量')
    return
  }
  submitting.value = true
  try {
    result.value = await createAdjustment({
      batchId: form.batchId,
      reasonId: form.reasonId,
      newQuantity: form.newQuantity,
      note: form.note || undefined
    })
    ElMessage.success('调整已创建')
  } catch {
    // 错误提示已由 http 拦截器统一处理
  } finally {
    submitting.value = false
  }
}

function goDetail() {
  if (result.value) {
    router.push(`/adjustments/${result.value.id}`)
  }
}
</script>
