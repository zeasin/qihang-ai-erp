import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/Home.vue'),
    },
    {
      path: '/chat',
      name: 'chat',
      component: () => import('../views/Chat.vue'),
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: () => import('../views/Dashboard.vue'),
    },

    // ─── 角色工作台 ───
    {
      path: '/workspace',
      name: 'workspace',
      redirect: '/workspace/order',
      children: [
        {
          path: 'order',
          name: 'workspace-order',
          component: () => import('../views/workspace/OrderWorkspace.vue'),
        },
        {
          path: 'picking',
          name: 'workspace-picking',
          component: () => import('../views/workspace/PickingWorkspace.vue'),
        },
        {
          path: 'packing',
          name: 'workspace-packing',
          component: () => import('../views/workspace/PackingWorkspace.vue'),
        },
        {
          path: 'shipping',
          name: 'workspace-shipping',
          component: () => import('../views/workspace/ShippingWorkspace.vue'),
        },
        {
          path: 'receiving',
          name: 'workspace-receiving',
          component: () => import('../views/workspace/ReceivingWorkspace.vue'),
        },
        {
          path: 'qc',
          name: 'workspace-qc',
          component: () => import('../views/workspace/QCWorkspace.vue'),
        },
        {
          path: 'counting',
          name: 'workspace-counting',
          component: () => import('../views/workspace/CountingWorkspace.vue'),
        },
      ],
    },
  ],
})

export default router