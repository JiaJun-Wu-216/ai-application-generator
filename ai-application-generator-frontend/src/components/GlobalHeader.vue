<template>
  <a-layout-header class="global-header">
    <div class="header-left">
      <router-link to="/" class="logo-wrapper">
        <img src="../assets/logo.png" alt="logo" class="logo-img" />
        <span class="site-title">AI 应用生成平台</span>
      </router-link>
    </div>
    <div class="header-center">
      <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="horizontal"
        :items="menuItems"
        @click="handleMenuClick"
      />
    </div>
    <div class="header-right">
      <a-button type="primary" @click="handleLogin">登录</a-button>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import type {MenuProps} from 'ant-design-vue'

const router = useRouter()

const menuItems = ref([{ key: '/', label: '首页', title: '首页' }])

const selectedKeys = ref<string[]>(['/'])
// 监听路由变化，更新当前选中菜单
router.afterEach((to) => {
  selectedKeys.value = [to.path]
})

const handleMenuClick: MenuProps['onClick'] = (e) => {
  const key = e.key as string
  selectedKeys.value = [key]
  // 跳转到对应页面
  if (key.startsWith('/')) {
    router.push(key)
  }
}

function handleLogin() {}
</script>

<style scoped>
.global-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: #fff;
}

.header-left {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.logo-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
}

.logo-img {
  width: 36px;
  height: 36px;
  object-fit: contain;
}

.site-title {
  font-size: 18px;
  font-weight: 600;
  color: #1890ff;
  white-space: nowrap;
}

.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
  min-width: 0;
}

.header-center :deep(.ant-menu) {
  border-bottom: none;
}

.header-right {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  margin-left: 16px;
}

@media (max-width: 768px) {
  .global-header {
    padding: 0 12px;
  }

  .site-title {
    font-size: 16px;
  }

  .logo-img {
    width: 28px;
    height: 28px;
  }
}

@media (max-width: 480px) {
  .site-title {
    display: none;
  }
}
</style>
