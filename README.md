# optimisticgirl

自定义HTTP请求包的批量请求器，可以从大量请求中过滤特定的数据包。
![image](https://user-images.githubusercontent.com/120230212/229114119-2d5a6b7c-6077-4a1d-b722-43da5c023882.png)

# 查询语句

```
statuscode = "200"
statuscode != "200"
statuscode > "200"
statuscode < "404"
statuscode >= "200"
statuscode <= "200"
content-length = "200"
content-length != "200"
content-length > "200"
content-length < "404"
content-length >= "200"
content-length <= "200"
location.contain = "login"
location.contain != "login"
content-type.contain = "text/html"
content-type.contain != "text/html"
header.contain = "rememberMe=deleteMe"
header.contain != "rememberMe=deleteMe"
body.contain = "Whitelabel Error Page"
body.contain != "Whitelabel Error Page"
body.contain = "默认密码"
body.contain != "站点关闭"
```

# 注意的事情

- 搜索语句可以组合起来使用，但仅限于逻辑与组合，可以通过多次查询达到逻辑或的效果。比如 `body.contain = "Whitelabel Error Page" || header.contain = "rememberMe=deleteMe" ` 可以分两次查询，查询的结果是累加的，并不会在查询前把结果清除掉。
- 导出结果到 CSV 文件时不会把响应包也写在 CSV 文件中，因为内容量太大影响观看。推荐导出结果到 TXT 文件和 CSV 文件，TXT 文件用于还原结果，CSV 文件用于快速查看大量资产。
- 从 CSV 还原的结果无法用 `header.contain` 和 `body.contain` 搜索语句，还原数据请用 TXT 格式。
- 不要把线程设置太大。线程100时可正常工作，跑50000个目标用时15分钟。
- 不要一次性跑太多目标，否则内存消耗过大导致崩溃，一次跑50000个目标是可以接受的范围。
- 进度在命令行中查看。

# 免责声明

本工具仅面向合法授权的企业安全建设行为，如您需要测试本工具的可用性，请自行搭建靶机环境。

在使用本工具进行检测时，您应确保该行为符合当地的法律法规，并且已经取得了足够的授权。请勿对非授权目标进行扫描。

如您在使用本工具的过程中存在任何非法行为，您需自行承担相应后果，我们将不承担任何法律及连带责任。

在安装并使用本工具前，请您务必审慎阅读、充分理解各条款内容，限制、免责条款或者其他涉及您重大权益的条款可能会以加粗、加下划线等形式提示您重点注意。 除非您已充分阅读、完全理解并接受本协议所有条款，否则，请您不要安装并使用本工具。您的使用行为或者您以其他任何明示或者默示方式表示接受本协议的，即视为您已阅读并同意本协议的约束。

# 参考

https://github.com/wgpsec/fofa_viewer

https://github.com/ScriptKid-Beta/WebBatchRequest
