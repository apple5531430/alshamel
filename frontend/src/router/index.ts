import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue')
    },
    {
      path: '/',
      component: () => import('@/views/LayoutView.vue'),
      redirect: '/inventory',
      children: [
        {
          path: 'products',
          name: 'products',
          component: () => import('@/views/ProductsView.vue')
        },
        {
          path: 'inventory',
          name: 'inventory',
          component: () => import('@/views/InventoryView.vue')
        },
        {
          path: 'reasons',
          name: 'reasons',
          component: () => import('@/views/ReasonsView.vue')
        },
        {
          path: 'adjustments',
          name: 'adjustments',
          component: () => import('@/views/AdjustmentsView.vue')
        },
        {
          path: 'adjustments/new',
          name: 'adjustment-create',
          component: () => import('@/views/CreateAdjustmentView.vue')
        },
        {
          path: 'adjustments/batch',
          name: 'adjustment-batch',
          component: () => import('@/views/BatchAdjustmentView.vue')
        },
        {
          path: 'adjustments/:id',
          name: 'adjustment-detail',
          component: () => import('@/views/AdjustmentDetailView.vue')
        }
      ]
    }
  ]
})

// 路由守卫：未登录一律跳转登录页
router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  if (to.name !== 'login' && !token) {
    return { name: 'login' }
  }
  if (to.name === 'login' && token) {
    return { name: 'inventory' }
  }
})

export default router
