export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  tokenType: string
  username: string
  role: string
  expiresInMs: number
}

export interface AdjustmentReason {
  id: number
  code: string
  name: string
  type: string
  active: boolean
}

export interface Product {
  id: number
  sku: string
  name: string
  unit: string
}

export interface BatchView {
  id: number
  batchNo: string
  quantity: number
  productSku: string
  productName: string
  productUnit: string
  warehouseCode: string
  warehouseName: string
}

export interface CreateAdjustmentRequest {
  batchId: number
  reasonId: number
  newQuantity: number
  note?: string
}

export interface AdjustmentItem {
  batchId: number
  reasonId: number
  newQuantity: number
  note?: string
}

export interface BatchAdjustmentRequest {
  items: AdjustmentItem[]
}

export interface InventoryAdjustmentView {
  id: number
  oldQuantity: number
  newQuantity: number
  quantityDiff: number
  note: string
  createdAt: string
  adjustedBy: string
  batch: {
    id: number
    batchNo: string
    quantity: number
    product: { id: number; sku: string; name: string; unit: string }
    warehouse: { id: number; code: string; name: string; location: string }
  }
  reason: { id: number; code: string; name: string }
}
