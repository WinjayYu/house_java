define({
  "name": "IVF接口文档",
  "version": "0.0.1",
  "description": "",
  "title": "Custom apiDoc browser title",
  "url": "http://14.17.81.101:8080/ivf",
  "sampleUrl": "http://14.17.81.101:8080/ivf",
  "header": {
    "title": "start",
    "content": "<h3>接口返回格式</h3>\n<h2>单条数据:</h2>\n<blockquote>\n<p>异常:</p>\n</blockquote>\n<pre><code class=\"language-javascript\">{ \n &quot;status&quot;:500,\n &quot;data&quot;:{},\n &quot;msg&quot;:&quot;错误消息&quot;\n}\n</code></pre>\n<blockquote>\n<p>正常:</p>\n</blockquote>\n<pre><code class=\"language-javascript\">{\n &quot;status&quot;:200,\n &quot;data&quot;:{&quot;id&quot;:&quot;1&quot;,&quot;name&quot;:&quot;wb&quot;},\n &quot;msg&quot;:&quot;&quot;\n}\n</code></pre>\n<h2>多条数据:</h2>\n<blockquote>\n<p>异常:</p>\n</blockquote>\n<pre><code class=\"language-javascript\">{\n &quot;status&quot;:500,\n &quot;data&quot;:{},\n &quot;msg&quot;:&quot;错误消息&quot;\n}\n</code></pre>\n<blockquote>\n<p>正常:</p>\n</blockquote>\n<pre><code class=\"language-javascript\">{\n &quot;status&quot;:200,\n &quot;data&quot;:{\n   &quot;list&quot;:[\n     {&quot;id&quot;:267,&quot;city&quot;:&quot;Wuhan&quot;},\n     {&quot;id&quot;:266,&quot;city&quot;:&quot;Nanjin&quot;}\n   ],\n   &quot;page&quot;:{\n     &quot;totalNum&quot;:4,\n     &quot;totalPage&quot;:2,\n     &quot;currentPage&quot;:1\n   }\n }  \n}\n</code></pre>\n"
  },
  "template": {
    "forceLanguage": "zh",
    "withCompare": true,
    "withGenerator": true
  },
  "apidoc": "0.2.0",
  "generator": {
    "name": "apidoc",
    "time": "2016-10-31T02:08:00.541Z",
    "url": "http://apidocjs.com",
    "version": "0.16.1"
  }
});
