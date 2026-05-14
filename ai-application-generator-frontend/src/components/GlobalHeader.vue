<template>
  <a-layout-header class="header">
    <a-row :wrap="false">
      <!-- 左侧：Logo和标题 -->
      <a-col flex="200px">
        <RouterLink to="/">
          <div class="header-left">
            <img class="logo" src="@/assets/logo.png" alt="Logo" />
            <h1 class="site-title">AI 应用生成平台</h1>
          </div>
        </RouterLink>
      </a-col>
      <!-- 中间：导航菜单 -->
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="horizontal"
          :items="menuItems"
          @click="handleMenuClick"
        />
      </a-col>
      <!-- 右侧：用户操作区域 -->
      <a-col>
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser?.id">
            <a-dropdown>
              <a-space>
                <a-avatar :src="loginUserStore.loginUser.userAvatar" />
                {{ loginUserStore.loginUser.userName ?? '无名' }}
              </a-space>
              <template #overlay>
                <a-menu>
                  <a-menu-item @click="doLogout">
                    <LogoutOutlined />
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </a-layout-header>
</template>

<script setup lang="ts">
import {computed, h, ref} from 'vue'
import {type RouteRecordNormalized, useRouter} from 'vue-router'
import type {MenuProps} from 'ant-design-vue'
import {message} from 'ant-design-vue'
import {useLoginUserStore} from '@/stores/LoginUser.ts'
import {HomeOutlined, LogoutOutlined} from '@ant-design/icons-vue'
import {userLogout} from '@/api/UserController.ts'
import {checkAccess} from '@/access/ChcekAccess.ts'
import type {LoginUserVO} from '@/entity/User.ts'

const router = useRouter()
const loginUserStore = useLoginUserStore()

interface menuItem {
  key: string
  label: string
  title: string
}

// 菜单配置项
const originItems = [
  {
    key: '/',
    label: '主页',
    title: '主页',
    icon: () => h(HomeOutlined),
  },
  {
    key: '/admin/userManage',
    label: '用户管理',
    title: '用户管理',
  },
]

const menuToRouteItem = (menu: menuItem) => {
  return router.getRoutes().find((route) => route.path === menu.key)
}

// 过滤菜单项
const filterMenus = (menus = [] as MenuProps['items']) => {
  return menus?.filter((menu) => {
    const item = menuToRouteItem(menu as menuItem) as RouteRecordNormalized
    if (item.meta?.hideInMenu) {
      return false
    }
    // 根据权限过滤菜单，有权限则返回 true，则保留该菜单
    return checkAccess(loginUserStore.loginUser as LoginUserVO, item.meta?.access as string)
  })
}

const menuItems = computed<MenuProps['items']>(() => filterMenus(originItems))

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

// 用户注销
const doLogout = async () => {
  const response = await userLogout()
  if (response.code === 0) {
    loginUserStore.logout()
    message.success('退出登录成功')
    await router.push('/')
  } else {
    message.error('退出登录失败，' + response.message)
  }
}
</script>

<style scoped>
.header {
  background: #fff;
  padding: 0 24px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  height: 48px;
  width: 48px;
}

.site-title {
  margin: 0;
  font-size: 18px;
  color: #1890ff;
}

.ant-menu-horizontal {
  border-bottom: none !important;
}
</style>
