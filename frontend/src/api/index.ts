import http from './http'
import type {
  ApiResponse,
  AdjustmentReason,
  BatchAdjustmentRequest,
  BatchView,
  CreateAdjustmentRequest,
  InventoryAdjustmentView,
  LoginRequest,
  LoginResponse,
  Product
} from '@/types'

async function get<T>(url: string): Promise<T> {
  const res = (await http.get(url)) as unknown as ApiResponse<T>
  return res.data
}

async function post<T>(url: string, body?: unknown): Promise<T> {
  const res = (await http.post(url, body)) as unknown as ApiResponse<T>
  return res.data
}

async function put<T>(url: string, body?: unknown): Promise<T> {
  const res = (await http.put(url, body)) as unknown as ApiResponse<T>
  return res.data
}

export const login = (body: LoginRequest) => post<LoginResponse>('/auth/login', body)

// 调整下拉用：启用 + 适用于库存调整
export const fetchReasons = () => get<AdjustmentReason[]>('/adjustment-reasons/available')

// 管理页用：全部原因
export const fetchAllReasons = () => get<AdjustmentReason[]>('/adjustment-reasons')

export const updateReasonActive = (id: number, active: boolean) =>
  put<AdjustmentReason>(`/adjustment-reasons/${id}/active`, { active })

export const fetchBatches = () => get<BatchView[]>('/batches')

export const fetchProducts = () => get<Product[]>('/products')

export const fetchAdjustments = () => get<InventoryAdjustmentView[]>('/inventory-adjustments')

export const createAdjustment = (body: CreateAdjustmentRequest) =>
  post<InventoryAdjustmentView>('/inventory-adjustments', body)

export const createBatchAdjustments = (body: BatchAdjustmentRequest) =>
  post<InventoryAdjustmentView[]>('/inventory-adjustments/batch', body)

export const fetchAdjustment = (id: number) =>
  get<InventoryAdjustmentView>(`/inventory-adjustments/${id}`)
