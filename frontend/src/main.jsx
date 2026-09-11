import React, { useEffect, useMemo, useRef, useState } from 'react'
import { createRoot } from 'react-dom/client'
import QRCode from 'qrcode'
import './styles.css'

const NAV_ITEMS = [
  { id: 'files', label: '文件', icon: '▦' },
  { id: 'search', label: '搜索', icon: '⌕' },
  { id: 'shares', label: '分享', icon: '↗' },
  { id: 'sharePage', label: '分享页', icon: '◇' },
  { id: 'login', label: '扫码登录', icon: '▣' }
]

function App() {
  const [active, setActive] = useState('files')
  const [cookie, setCookie] = useState(() => localStorage.getItem('quark-cookie') || '')
  const [cookieVisible, setCookieVisible] = useState(false)
  const [toast, setToast] = useState(null)

  useEffect(() => {
    localStorage.setItem('quark-cookie', cookie)
  }, [cookie])

  function notify(message, type = 'info') {
    setToast({ message, type })
    window.setTimeout(() => setToast(null), 3600)
  }

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-mark">C</div>
          <div>
            <strong>CloudDrive</strong>
            <span>QUARK PROXY</span>
          </div>
        </div>
        <div className="side-label">工作台</div>
        <nav className="nav-list">
          {NAV_ITEMS.map(item => (
            <button
              key={item.id}
              className={active === item.id ? 'nav-item active' : 'nav-item'}
              onClick={() => setActive(item.id)}
            >
              <span className="nav-icon">{item.icon}</span>
              {item.label}
            </button>
          ))}
        </nav>
        <div className="sidebar-foot">
          <div className="secure-dot" />
          <div>
            <strong>本地代理</strong>
            <span>Cookie 只在本机转发</span>
          </div>
        </div>
      </aside>

      <main className="main-area">
        <header className="topbar">
          <div>
            <p className="eyebrow">CLOUD DRIVE / QUARK API</p>
            <h1>{NAV_ITEMS.find(item => item.id === active)?.label}</h1>
          </div>
          <div className="top-actions">
            <div className="connection-pill"><span /> Spring Boot proxy · 8080</div>
            <a className="docs-link" href="/README.md" onClick={event => event.preventDefault()}>API 文档</a>
          </div>
        </header>

        <section className="auth-bar card">
          <div className="auth-copy">
            <div className="auth-badge">AUTH</div>
            <div>
              <strong>夸克 Cookie</strong>
              <span>请求会通过 Java 代理转发到 quark.cn；不会发送到第三方。</span>
            </div>
          </div>
          <div className="cookie-editor">
            <input
              type={cookieVisible ? 'text' : 'password'}
              value={cookie}
              onChange={event => setCookie(event.target.value)}
              placeholder="粘贴 Cookie，或使用 QUARK_COOKIE 环境变量"
              aria-label="夸克 Cookie"
            />
            <button className="icon-button" onClick={() => setCookieVisible(value => !value)}>{cookieVisible ? '隐藏' : '显示'}</button>
            <button className="ghost-button" onClick={() => { setCookie(''); localStorage.removeItem('quark-cookie') }}>清除</button>
          </div>
        </section>

        {active === 'files' && <FilesPanel cookie={cookie} notify={notify} />}
        {active === 'search' && <SearchPanel cookie={cookie} notify={notify} />}
        {active === 'shares' && <SharesPanel cookie={cookie} notify={notify} />}
        {active === 'sharePage' && <SharePagePanel cookie={cookie} notify={notify} />}
        {active === 'login' && <LoginPanel notify={notify} onCookie={setCookie} />}
      </main>

      {toast && <div className={`toast ${toast.type}`}>{toast.message}</div>}
    </div>
  )
}

function FilesPanel({ cookie, notify }) {
  const [folderId, setFolderId] = useState('0')
  const [folderName, setFolderName] = useState('')
  const [files, setFiles] = useState([])
  const [selected, setSelected] = useState([])
  const [loading, setLoading] = useState(false)
  const [raw, setRaw] = useState(null)

  async function loadFiles() {
    setLoading(true)
    try {
      const result = await api(`/api/quark/files?pdirFid=${encodeURIComponent(folderId)}&page=1&size=50`, { cookie })
      setRaw(result)
      setFiles(normalizeFiles(result))
      setSelected([])
    } catch (error) {
      notify(error.message, 'error')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { loadFiles() }, [folderId])

  async function createFolder() {
    if (!folderName.trim()) return notify('先输入文件夹名称', 'error')
    try {
      await api('/api/quark/files/folders', {
        method: 'POST', cookie,
        body: { pdirFid: folderId, fileName: folderName.trim() }
      })
      setFolderName('')
      notify('创建文件夹请求已发送', 'success')
      loadFiles()
    } catch (error) { notify(error.message, 'error') }
  }

  async function deleteSelected() {
    if (!selected.length) return notify('先选择要删除的文件', 'error')
    if (!window.confirm(`确认删除选中的 ${selected.length} 个项目？`)) return
    try {
      await api('/api/quark/files/delete', {
        method: 'POST', cookie,
        body: { filelist: selected, excludeFids: [] }
      })
      notify('删除请求已发送', 'success')
      loadFiles()
    } catch (error) { notify(error.message, 'error') }
  }

  async function downloadSelected() {
    if (!selected.length) return notify('先选择文件', 'error')
    try {
      const result = await api('/api/quark/files/download', {
        method: 'POST', cookie, body: { fids: selected }
      })
      const links = extractDownloadLinks(result)
      if (!links.length) return notify('上游没有返回下载直链，请查看右侧响应', 'error')
      links.forEach(link => window.open(link, '_blank', 'noopener,noreferrer'))
      notify(`已打开 ${links.length} 个下载链接`, 'success')
    } catch (error) { notify(error.message, 'error') }
  }

  function toggleSelected(fid) {
    setSelected(current => current.includes(fid) ? current.filter(item => item !== fid) : [...current, fid])
  }

  return (
    <div className="content-grid">
      <section className="main-card card">
        <div className="section-head">
          <div>
            <div className="breadcrumb"><button onClick={() => setFolderId('0')}>我的网盘</button><span>/</span><code>{folderId}</code></div>
            <h2>文件浏览器</h2>
            <p>代理接口：<code>GET /api/quark/files</code></p>
          </div>
          <button className="primary-button" onClick={loadFiles}>{loading ? '加载中…' : '刷新'}</button>
        </div>
        <div className="toolbar">
          <div className="inline-form">
            <input value={folderName} onChange={event => setFolderName(event.target.value)} placeholder="新建文件夹" onKeyDown={event => event.key === 'Enter' && createFolder()} />
            <button className="secondary-button" onClick={createFolder}>新建</button>
          </div>
          <div className="toolbar-actions">
            <button className="ghost-button" onClick={downloadSelected}>下载直链</button>
            <button className="danger-button" onClick={deleteSelected}>删除</button>
          </div>
        </div>
        <div className="table-wrap">
          <table>
            <thead><tr><th className="check-col"><input type="checkbox" checked={files.length > 0 && selected.length === files.length} onChange={event => setSelected(event.target.checked ? files.map(file => file.id) : [])} /></th><th>名称</th><th>类型</th><th>大小</th><th>更新时间</th></tr></thead>
            <tbody>
              {files.map(file => (
                <tr key={file.id} className={selected.includes(file.id) ? 'selected-row' : ''} onDoubleClick={() => file.isDir && setFolderId(file.id)}>
                  <td className="check-col"><input type="checkbox" checked={selected.includes(file.id)} onChange={() => toggleSelected(file.id)} onClick={event => event.stopPropagation()} /></td>
                  <td><div className="file-name"><span className={file.isDir ? 'file-icon folder' : 'file-icon'}>{file.isDir ? '□' : '•'}</span><strong>{file.name}</strong></div></td>
                  <td>{file.isDir ? '文件夹' : file.type || '文件'}</td>
                  <td>{file.isDir ? '—' : formatSize(file.size)}</td>
                  <td>{formatDate(file.updatedAt)}</td>
                </tr>
              ))}
              {!loading && files.length === 0 && <tr><td colSpan="5"><EmptyState title="还没有文件数据" text={cookie ? '点击刷新，或确认 Cookie 是否有效。' : '先在顶部粘贴 Cookie，再点击刷新。'} /></td></tr>}
              {loading && <tr><td colSpan="5"><div className="loading-row">正在请求夸克上游…</div></td></tr>}
            </tbody>
          </table>
        </div>
      </section>
      <aside className="side-stack">
        <ResponseCard data={raw} />
        <div className="card tip-card"><span className="tip-icon">i</span><div><strong>双击文件夹进入</strong><p>移动、删除、下载接口都使用当前选中的文件 ID；异步任务的 <code>task_id</code> 可在接口响应中继续轮询。</p></div></div>
      </aside>
    </div>
  )
}

function SearchPanel({ cookie, notify }) {
  const [query, setQuery] = useState('')
  const [files, setFiles] = useState([])
  const [raw, setRaw] = useState(null)
  const [loading, setLoading] = useState(false)
  async function search() {
    if (!query.trim()) return notify('请输入关键词', 'error')
    setLoading(true)
    try {
      const result = await api(`/api/quark/files/search?q=${encodeURIComponent(query.trim())}&page=1&size=50`, { cookie })
      setRaw(result); setFiles(normalizeFiles(result))
    } catch (error) { notify(error.message, 'error') } finally { setLoading(false) }
  }
  return <div className="content-grid"><section className="main-card card"><div className="section-head"><div><h2>搜索文件</h2><p>代理接口：<code>GET /api/quark/files/search</code></p></div></div><div className="search-box"><input value={query} onChange={event => setQuery(event.target.value)} placeholder="输入文件名或关键词" onKeyDown={event => event.key === 'Enter' && search()} /><button className="primary-button" onClick={search}>{loading ? '搜索中…' : '搜索'}</button></div><div className="search-results">{files.map(file => <div className="result-row" key={file.id}><span className={file.isDir ? 'file-icon folder' : 'file-icon'}>{file.isDir ? '□' : '•'}</span><div><strong>{file.name}</strong><span>{file.id} · {formatSize(file.size)}</span></div></div>)}{!loading && files.length === 0 && <EmptyState title="输入关键词开始搜索" text="高级大小/扩展名过滤由前端继续筛选。" />}</div></section><aside className="side-stack"><ResponseCard data={raw} /></aside></div>
}

function SharesPanel({ cookie, notify }) {
  const [shareId, setShareId] = useState('')
  const [title, setTitle] = useState('CloudDrive 分享')
  const [fidList, setFidList] = useState('')
  const [shares, setShares] = useState([])
  const [raw, setRaw] = useState(null)
  async function load() { try { const result = await api('/api/quark/shares?page=1&size=50', { cookie }); setRaw(result); setShares(normalizeShares(result)) } catch (error) { notify(error.message, 'error') } }
  async function create() { const ids = fidList.split(/[\s,，]+/).filter(Boolean); if (!ids.length) return notify('填写至少一个文件 ID', 'error'); try { const result = await api('/api/quark/shares', { method: 'POST', cookie, body: { fidList: ids, title, urlType: 1, expiredType: 1 } }); setRaw(result); notify('创建分享任务已发送', 'success'); load() } catch (error) { notify(error.message, 'error') } }
  async function getUrl() { if (!shareId.trim()) return notify('填写分享 ID', 'error'); try { const result = await api('/api/quark/shares/url', { method: 'POST', cookie, body: { shareId: shareId.trim() } }); setRaw(result); notify('已获取分享链接', 'success') } catch (error) { notify(error.message, 'error') } }
  return <div className="content-grid"><section className="main-card card"><div className="section-head"><div><h2>我的分享</h2><p>创建分享、获取分享链接、查看分享列表。</p></div><button className="primary-button" onClick={load}>刷新列表</button></div><div className="form-grid"><label>文件 ID（可多个）<input value={fidList} onChange={event => setFidList(event.target.value)} placeholder="用空格或逗号分隔" /></label><label>分享标题<input value={title} onChange={event => setTitle(event.target.value)} /></label></div><div className="button-row"><button className="primary-button" onClick={create}>创建分享</button><input value={shareId} onChange={event => setShareId(event.target.value)} placeholder="分享 ID" /><button className="secondary-button" onClick={getUrl}>获取链接</button></div><div className="share-list">{shares.map(share => <div className="share-row" key={share.id}><div><strong>{share.title || share.id}</strong><span>{share.url || '等待任务完成'} · {formatDate(share.createdAt)}</span></div><code>{share.id}</code></div>)}{!shares.length && <EmptyState title="暂无分享记录" text="需要有效 Cookie 才能访问夸克分享接口。" />}</div></section><aside className="side-stack"><ResponseCard data={raw} /></aside></div>
}

function SharePagePanel({ cookie, notify }) {
  const [pwdId, setPwdId] = useState('')
  const [passcode, setPasscode] = useState('')
  const [stoken, setStoken] = useState('')
  const [raw, setRaw] = useState(null)
  const [items, setItems] = useState([])
  async function getToken() { if (!pwdId.trim()) return notify('填写分享 ID 或分享链接中的 /s/ 后缀', 'error'); try { const result = await api('/api/quark/share-page/token', { method: 'POST', cookie, body: { pwdId: pwdId.trim(), passcode } }); setRaw(result); const token = findValue(result, ['stoken', 'stoken']); if (token) setStoken(token); notify('分享页 Token 获取成功', 'success') } catch (error) { notify(error.message, 'error') } }
  async function browse() { if (!pwdId.trim() || !stoken.trim()) return notify('先获取 stoken', 'error'); try { const result = await api(`/api/quark/share-page/files?pwdId=${encodeURIComponent(pwdId)}&stoken=${encodeURIComponent(stoken)}&pdirFid=0&page=1&size=50`, { cookie }); setRaw(result); setItems(normalizeFiles(result)) } catch (error) { notify(error.message, 'error') } }
  return <div className="content-grid"><section className="main-card card"><div className="section-head"><div><h2>分享页浏览</h2><p>先换取 stoken，再浏览分享内容；后续可调用转存接口。</p></div></div><div className="form-grid"><label>分享 ID / pwd_id<input value={pwdId} onChange={event => setPwdId(event.target.value)} placeholder="例如：分享链接 /s/ 后面的 ID" /></label><label>提取码（没有则留空）<input value={passcode} onChange={event => setPasscode(event.target.value)} /></label><label className="full-width">stoken<input value={stoken} onChange={event => setStoken(event.target.value)} placeholder="点击获取 Token 后自动填入" /></label></div><div className="button-row"><button className="primary-button" onClick={getToken}>获取 Token</button><button className="secondary-button" onClick={browse}>浏览内容</button></div><div className="share-list">{items.map(item => <div className="share-row" key={item.id}><div><strong>{item.name}</strong><span>{item.id} · {item.isDir ? '文件夹' : formatSize(item.size)}</span></div><span className="tag">{item.isDir ? 'DIR' : 'FILE'}</span></div>)}{!items.length && <EmptyState title="还没有浏览结果" text="分享页接口使用 drive.quark.cn，与主网盘接口域名不同。" />}</div></section><aside className="side-stack"><ResponseCard data={raw} /></aside></div>
}

function LoginPanel({ notify, onCookie }) {
  const [qr, setQr] = useState(null)
  const [image, setImage] = useState('')
  const [status, setStatus] = useState('点击按钮生成二维码')
  const timer = useRef(null)

  useEffect(() => () => window.clearInterval(timer.current), [])

  async function createQr() {
    try {
      const result = await api('/api/quark/auth/qrcode')
      setQr(result)
      setImage(await QRCode.toDataURL(result.qrUrl, { width: 240, margin: 2, color: { dark: '#101828', light: '#ffffff' } }))
      setStatus('请使用夸克 App 扫码，页面会自动轮询状态')
      window.clearInterval(timer.current)
      timer.current = window.setInterval(() => poll(result.token), 2200)
    } catch (error) { notify(error.message, 'error') }
  }

  async function poll(token) {
    try {
      const result = await api(`/api/quark/auth/qrcode/status?token=${encodeURIComponent(token)}`)
      const statusCode = String(findValue(result, ['status', 'status_code']) || '')
      if (statusCode === '50004001') return setStatus('等待扫码确认…')
      const ticket = findValue(result, ['service_ticket', 'serviceTicket'])
      if (ticket) {
        window.clearInterval(timer.current)
        setStatus('扫码成功，正在换取 Cookie…')
        const session = await api(`/api/quark/auth/session?ticket=${encodeURIComponent(ticket)}`, { raw: true })
        const cookie = session.headers?.get('X-Quark-Cookie')
        if (cookie) {
          onCookie(cookie)
          setStatus('Cookie 已填入顶部输入框，请点击文件页刷新')
        } else {
          setStatus('扫码成功；请从响应或浏览器中复制 Cookie 到顶部')
        }
      } else {
        setStatus(`扫码状态：${statusCode || '处理中'}`)
      }
    } catch (error) {
      window.clearInterval(timer.current)
      setStatus(error.message)
    }
  }

  return <div className="login-layout"><section className="login-card card"><div className="section-head"><div><h2>扫码登录</h2><p>二维码由 Java 后端根据文档中的 CAS 接口生成。</p></div><button className="primary-button" onClick={createQr}>生成二维码</button></div><div className="qr-stage">{image ? <img src={image} alt="夸克扫码登录二维码" /> : <div className="qr-placeholder">QR<span>等待生成</span></div>}<strong>{status}</strong>{qr && <code>{qr.token}</code>}</div></section><section className="card instruction-card"><span className="tip-icon">!</span><div><h3>安全提示</h3><p>这是基于逆向接口的本地学习项目，不是夸克官方 SDK。Cookie 具有账号权限，不要提交到 Git 或分享给他人。</p><ol><li>生成二维码并用夸克 App 扫码。</li><li>成功后检查顶部 Cookie 输入框。</li><li>回到“文件”页刷新网盘列表。</li></ol></div></section></div>
}

function ResponseCard({ data }) {
  return <div className="response-card card"><div className="response-head"><strong>上游响应</strong><span>JSON</span></div><pre>{data ? JSON.stringify(data, null, 2) : '点击接口后，这里显示夸克原始响应。'}</pre></div>
}

function EmptyState({ title, text }) { return <div className="empty-state"><div className="empty-icon">⌁</div><strong>{title}</strong><span>{text}</span></div> }

async function api(url, { method = 'GET', body, cookie = '', raw = false } = {}) {
  const headers = { Accept: 'application/json' }
  if (cookie) headers['X-Quark-Cookie'] = cookie
  if (body !== undefined) headers['Content-Type'] = 'application/json'
  const response = await fetch(url, { method, headers, body: body === undefined ? undefined : JSON.stringify(body) })
  if (raw) return response
  const text = await response.text()
  let result
  try { result = text ? JSON.parse(text) : null } catch { result = text }
  if (!response.ok) throw new Error(result?.msg || result?.message || `请求失败：HTTP ${response.status}`)
  return result
}

function normalizeFiles(result) {
  const list = result?.data?.list || result?.data?.items || result?.data?.file_list || (Array.isArray(result?.data) ? result.data : [])
  return (Array.isArray(list) ? list : []).map(item => ({
    id: String(item.fid || item.file_id || item.id || item.fileId || ''),
    name: item.file_name || item.fileName || item.name || '未命名',
    size: Number(item.size || item.file_size || 0),
    type: item.mime_type || item.file_type_name || item.format_type || '',
    updatedAt: item.updated_at || item.updatedAt || item.updated_at_str,
    isDir: Boolean(item.dir || item.is_dir || item.file_type === 0 || item.category === 'folder')
  })).filter(item => item.id)
}

function normalizeShares(result) {
  const list = result?.data?.list || result?.data?.items || (Array.isArray(result?.data) ? result.data : [])
  return (Array.isArray(list) ? list : []).map(item => ({
    id: String(item.share_id || item.shareId || item.id || ''),
    title: item.title || item.share_title || '',
    url: item.share_url || item.shareUrl || '',
    createdAt: item.created_at || item.createdAt
  })).filter(item => item.id)
}

function extractDownloadLinks(result) {
  const list = Array.isArray(result?.data) ? result.data : result?.data?.list || []
  return (Array.isArray(list) ? list : []).map(item => item.download_url || item.url).filter(Boolean)
}

function findValue(root, names) {
  if (!root || typeof root !== 'object') return null
  for (const name of names) {
    if (root[name] !== undefined && root[name] !== null) return root[name]
  }
  for (const value of Object.values(root)) {
    if (value && typeof value === 'object') {
      const found = findValue(value, names)
      if (found !== null) return found
    }
  }
  return null
}

function formatSize(size) {
  if (!size) return '—'
  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  let value = size
  let index = 0
  while (value >= 1024 && index < units.length - 1) { value /= 1024; index += 1 }
  return `${value.toFixed(index === 0 ? 0 : 1)} ${units[index]}`
}

function formatDate(value) {
  if (!value) return '—'
  const date = new Date(typeof value === 'number' && value < 100000000000 ? value * 1000 : value)
  return Number.isNaN(date.getTime()) ? String(value) : date.toLocaleString('zh-CN', { hour12: false })
}

createRoot(document.getElementById('root')).render(<App />)
