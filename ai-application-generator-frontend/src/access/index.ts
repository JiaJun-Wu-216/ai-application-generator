import router from '@/router'
import {useLoginUserStore} from '@/stores/LoginUser.ts'
import {ACCESS_ENUM} from '@/access/AccessEnum.ts'
import {checkAccess} from '@/access/ChcekAccess.ts'
import {message} from 'ant-design-vue'

/**
 * 全局权限校验
 */
router.beforeEach(async (to, from) => {
  const loginUserStore = useLoginUserStore()
  let loginUser = loginUserStore.loginUser
  // 如果之前没登陆过，自动登录
  if (!loginUser || !loginUser.userRole) {
    // 加 await 是为了等用户登录成功之后，再执行后续的代码
    await loginUserStore.fetchLoginUser()
    loginUser = loginUserStore.loginUser
  }
  const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.NOT_LOGIN
  // 要跳转的页面必须已经登陆
  if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
    // 如果没登陆，跳转到登录页面
    if (!loginUser || !loginUser.userRole || loginUser.userRole === ACCESS_ENUM.NOT_LOGIN) {
      message.warn('未登录，请先登录')
      return `/user/login?redirect=${to.fullPath}`
    }
    // 如果已经登陆了，但是权限不足，那么跳转到无权限页面
    if (!checkAccess(loginUser, needAccess)) {
      message.error('无权限')
      return '/noAuth'
    }
  }
})
