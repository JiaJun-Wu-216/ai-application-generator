import {defineStore} from 'pinia'
import {ref} from 'vue'
import type {LoginUserVO} from '@/entity/User.ts'
import {getCurrentLoginUser} from '@/api/UserController.ts'

export const useLoginUserStore = defineStore('loginUser', () => {
  /**
   * 登陆用户信息
   */
  const loginUser = ref<LoginUserVO | null>(null)

  /**
   * 获取登录用户信息
   */
  async function fetchLoginUser() {
    const response = await getCurrentLoginUser()
    if (response.code === 0 && response.data) {
      loginUser.value = response.data
    }
  }

  /**
   * 设置用户信息
   * @param newLoginUser  新用户信息
   */
  function setLoginUser(newLoginUser: LoginUserVO) {
    loginUser.value = newLoginUser
  }

  /**
   * 退出登录
   */
  function logout() {
    loginUser.value = null
  }

  return { loginUser, setLoginUser, fetchLoginUser, logout }
})
