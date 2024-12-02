const getters = {
  sidebar: state => state.app.sidebar,
  token: state => state.user.accessToken,
  avatar: state => state.user.userInfo?.avatar || require('@/assets/avatar.png'),
  nickname: state => state.user.userInfo?.nickname || state.user.userInfo?.username || '',
  roles: state => state.user.roles,
  permission_routes: state => state.permission.routes,
  userInfo: state => state.user.userInfo
}

export default getters