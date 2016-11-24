define({ "api": [
  {
    "type": "get",
    "url": "/api/agenda/attendess/{id}",
    "title": "6.获取议程参会者列表",
    "version": "0.0.1",
    "name": "agenda_attendess",
    "group": "agenda",
    "description": "<p>获取议程参会者列表</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/AgendaController.java",
    "groupTitle": "agenda",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/agenda/attendess/{id}"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/agenda/detail/{id}",
    "title": "5.获取议程(洞见)详情",
    "version": "0.0.1",
    "name": "agenda_detail",
    "group": "agenda",
    "description": "<p>获取议程(洞见)详情</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          },
          {
            "group": "Success 200",
            "type": "Boolean",
            "optional": false,
            "field": "watchIs",
            "description": "<p>用户是否关注该议程</p>"
          },
          {
            "group": "Success 200",
            "type": "Integer",
            "optional": false,
            "field": "status",
            "description": "<p>状态 0(取消) 1(有效) 2(已结束)</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "description",
            "description": "<p>议程描述</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "aboutContent",
            "description": "<p>洞见详情</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "locationImgUrl",
            "description": "<p>议程地址图片url</p>"
          },
          {
            "group": "Success 200",
            "type": "List",
            "optional": false,
            "field": "lectors",
            "description": "<p>议程讲师列表</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\"status\": 200,\n\"msg\": \"议程详情\",\n\"data\": {\n\"watchIs\": false, // 用户是否关注该议程\n\"id\": 2,\n\"forum\": null,\n\"startDate\": \"2015-10-01 14:00:00\",\n\"endDate\": \"2015-10-01 16:00:00\",\n\"location\": \"深圳\",\n\"title\": \"Time lapes系统对胚胎选择\",\n\"status\": 1, // 状态 0(取消) 1(有效) 2(已结束)\n\"description\": null,\n\"bgImgUrl\": \"http://14.17.81.101:8080/files/upload/img/2016/8/893443466420637.png\",\n\"aboutContent\": \"洞见详情\",\n\"locationImgUrl\": \"议程地址的地图url\",\n\"hot\": 1,\n\"hotSort\": 3,\n\"lectors\": [\n  {\n     \"id\": 66,\n     \"name\": \"333333333333333333\",\n     \"password\": \"1231\",\n     \"description\": null,\n     \"ename\": \"1111\",\n     \"nationality\": \"1111\",\n     \"sex\": \"男\",\n     \"tel\": \"6\",\n     \"hospital\": \"111\",\n     \"post\": \"111\",\n     \"titles\": \"111\",\n     \"department\": \"111\",\n     \"imgUrl\": null,\n     \"email\": \"1234422@qq.com\",\n     \"remark\": \"111\",\n     \"type\": 1,\n     \"createDate\": \"2016-08-26 13:12:48\"\n  }\n] // 议程讲师信息\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/AgendaController.java",
    "groupTitle": "agenda",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/agenda/detail/{id}"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/agenda/document",
    "title": "9.议程相关文档(pdf)list",
    "version": "0.0.1",
    "name": "agenda_document",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "agendaId",
            "description": "<p>议程id</p>"
          }
        ]
      }
    },
    "group": "agenda",
    "description": "<p>返回议程相关文档</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/AgendaController.java",
    "groupTitle": "agenda",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/agenda/document"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/agenda/new",
    "title": "3、(议程)最新议程",
    "version": "0.0.1",
    "name": "agenda_new",
    "group": "agenda",
    "description": "<p>最新议程</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "forumId",
            "description": "<p>会议ID</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "pageNum",
            "description": "<p>页码</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "pageSize",
            "description": "<p>每页请求数</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "titile",
            "description": "<p>议程标题</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "startDate",
            "description": "<p>开始时间</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "endDate",
            "description": "<p>结束时间</p>"
          },
          {
            "group": "Success 200",
            "type": "INT",
            "optional": false,
            "field": "status",
            "description": "<p>状态（1启用，0未启用）</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "description",
            "description": "<p>议会内容</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n    \"status\": 200,\n    \"msg\": \"\",\n    \"data\": {\n    \"list\": [\n    {\n    \"id\": 1,\n    \"startDate\": \"2016-08-27 23:17:55\",\n    \"endDate\": \"2016-09-04 23:17:58\",\n    \"location\": \"beijin,dongjin,nanjin\",\n    \"title\": \"议会分享，脱欧之后\",\n    \"status\": 1,\n    \"description\": \"议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后\",\n    \"bgImgUrl\": null,\n    \"aboutContent\": null,\n    \"locationImgUrl\": null\n    },\n    {\n    \"id\": 2,\n    \"startDate\": \"2016-08-27 23:17:55\",\n    \"endDate\": \"2016-09-04 23:17:58\",\n    \"location\": \"beijin,dongjin,nanjin\",\n    \"title\": \"议会分享，脱欧之后\",\n    \"status\": 1,\n    \"description\": \"议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后\",\n    \"bgImgUrl\": \"\",\n    \"aboutContent\": \"\",\n    \"locationImgUrl\": \"\"\n    },\n    {\n    \"id\": 3,\n    \"startDate\": \"2016-08-27 23:17:55\",\n    \"endDate\": \"2016-09-04 23:17:58\",\n    \"location\": \"beijin,dongjin,nanjin\",\n    \"title\": \"议会分享，脱欧之后\",\n    \"status\": 1,\n    \"description\": \"议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后\",\n    \"bgImgUrl\": \"\",\n    \"aboutContent\": \"\",\n    \"locationImgUrl\": \"\"\n    },\n    {\n    \"id\": 4,\n    \"startDate\": \"2016-08-27 23:17:55\",\n    \"endDate\": \"2016-09-04 23:17:58\",\n    \"location\": \"beijin,dongjin,nanjin\",\n    \"title\": \"议会分享，脱欧之后\",\n    \"status\": 1,\n    \"description\": \"议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后\",\n    \"bgImgUrl\": \"\",\n    \"aboutContent\": \"\",\n    \"locationImgUrl\": \"\"\n    },\n    {\n    \"id\": 5,\n    \"startDate\": \"2016-08-27 23:17:55\",\n    \"endDate\": \"2016-09-04 23:17:58\",\n    \"location\": \"beijin,dongjin,nanjin\",\n    \"title\": \"议会分享，脱欧之后\",\n    \"status\": 1,\n    \"description\": \"议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后议会分享，脱欧之后\",\n    \"bgImgUrl\": \"\",\n    \"aboutContent\": \"\",\n    \"locationImgUrl\": \"\"\n    }\n    ],\n    \"page\": {\n    \"totalNum\": 6,\n    \"totalPage\": 2,\n    \"currentPage\": 1\n    }\n    }\n    }",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/AgendaController.java",
    "groupTitle": "agenda",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/agenda/new"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/agenda/query",
    "title": "4.议程综合查询接口",
    "version": "0.0.1",
    "name": "agenda_query",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "forum.id",
            "description": "<p>会议ID</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "tagId",
            "description": "<p>议程分类ID(反思创新... 等的id)</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "day",
            "description": "<p>根据时间获取议程 2015-10-1</p>"
          },
          {
            "group": "Parameter",
            "type": "Boolean",
            "optional": false,
            "field": "watch",
            "description": "<p>获取用户关注议程</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id(要和watch一起传入才能获取用户关注议程)</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "title",
            "description": "<p>议程名称</p>"
          }
        ]
      }
    },
    "group": "agenda",
    "description": "<p>议程综合查询接口(议程日历接口)</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/AgendaController.java",
    "groupTitle": "agenda",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/agenda/query"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/agenda/separate",
    "title": "2、(议程)不同形式的会议",
    "version": "0.0.1",
    "name": "agenda_separate",
    "group": "agenda",
    "description": "<p>不同形式的会议</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "forumId",
            "description": "<p>会议ID</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n    \"status\": 200,\n    \"msg\": \"\",\n    \"data\": [\n    {\n    \"id\": 4,\n    \"name\": \"协作区\",\n    \"description\": \"参与制定解决方案，打造涵盖各利益相关群体、由行动型领袖组成的圈子\",\n    \"bgImg\": null,\n    \"sort\": 1\n    },\n    {\n    \"id\": 5,\n    \"name\": \"探索区\",\n    \"description\": \"感受从虚拟现实到机器人等最新科技，展开体验式学习与思考。\",\n    \"bgImg\": null,\n    \"sort\": 2\n    },\n    {\n    \"id\": 6,\n    \"name\": \"讨论区\",\n    \"description\": \"深入了解时事，探讨当前经济、科技和政策辩论领域的核心观点和议题。\",\n    \"bgImg\": null,\n    \"sort\": 3\n    }\n    ]\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/AgendaController.java",
    "groupTitle": "agenda",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/agenda/separate"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/agenda/subject",
    "title": "1、(议程)会议主题",
    "version": "0.0.1",
    "name": "agenda_subject",
    "group": "agenda",
    "description": "<p>议程主题列表</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "forumId",
            "description": "<p>会议ID</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\"status\": 200,\n\"msg\": \"\",\n\"data\": {\n    \"list\": [\n    {\n    id: 1,\n    name: \"反思创新\",\n    description: \"基础研究、技术和商业模式\",\n    bgImg: null,\n    sort: 1\n    },\n    {\n    id: 2,\n    name: \"重塑增长\",\n    description: \"中国新出台的国民经济和社会发展五年规划\",\n    bgImg: null,\n    sort: 2\n    },\n    {\n    id: 3,\n    name: \"重设体系\",\n    description: \"第四次工业革命如何改变\",\n    bgImg: null,\n    sort: 3\n    }]\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/AgendaController.java",
    "groupTitle": "agenda",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/agenda/subject"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/agenda/unwatch",
    "title": "8.取消关注议程",
    "version": "0.0.1",
    "name": "agenda_unwatch",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "agendaId",
            "description": "<p>议程id</p>"
          }
        ]
      }
    },
    "group": "agenda",
    "description": "<p>取消关注议程</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/AgendaController.java",
    "groupTitle": "agenda",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/agenda/unwatch"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/agenda/watch",
    "title": "7.关注议程",
    "version": "0.0.1",
    "name": "agenda_watch",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "agendaId",
            "description": "<p>议程id</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "forumId",
            "description": "<p>会议ID</p>"
          }
        ]
      }
    },
    "group": "agenda",
    "description": "<p>关注议程</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/AgendaController.java",
    "groupTitle": "agenda",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/agenda/watch"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/app/version",
    "title": "app版本信息",
    "version": "0.0.1",
    "group": "app",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "optional": false,
            "field": "Result",
            "description": "<p>返回app 的版本信息和下载路径</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/AppController.java",
    "groupTitle": "app",
    "name": "GetApiAppVersion",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/app/version"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/about",
    "title": "公司介绍详情",
    "version": "0.0.1",
    "name": "api_about",
    "group": "app",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "optional": false,
            "field": "html",
            "description": "<p>这是一个html 页面直接通过webview 访问</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/AppController.java",
    "groupTitle": "app",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/about"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/about",
    "title": "隐私政策",
    "version": "0.0.1",
    "name": "api_about",
    "group": "app",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "optional": false,
            "field": "html",
            "description": "<p>这是一个html 页面直接通过webview 访问</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/AppController.java",
    "groupTitle": "app",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/about"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/forum/agendas/{id}",
    "title": "4.会议议程列表(洞见更新)",
    "version": "0.0.1",
    "name": "forum_agendas",
    "group": "forum",
    "description": "<p>获取会议议程列表</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/ForumControll.java",
    "groupTitle": "forum",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/forum/agendas/{id}"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/forum/detail/{id}",
    "title": "2.会议详情",
    "version": "0.0.1",
    "name": "forum_detail",
    "group": "forum",
    "description": "<p>获取会议详情 id = 0 默认返回最新的会议</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          },
          {
            "group": "Success 200",
            "type": "Integer",
            "optional": false,
            "field": "status",
            "description": "<p>会议状态 0未开始 1进行中 2已结束</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/ForumControll.java",
    "groupTitle": "forum",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/forum/detail/{id}"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/forum/hots/{id}",
    "title": "5.热点议程",
    "version": "0.0.1",
    "name": "forum_hots",
    "group": "forum",
    "description": "<p>获取会议热点议程</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/ForumControll.java",
    "groupTitle": "forum",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/forum/hots/{id}"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/forum/list",
    "title": "1.获取会议列表",
    "version": "0.0.1",
    "name": "forum_list",
    "group": "forum",
    "description": "<p>获取会议列表</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/ForumControll.java",
    "groupTitle": "forum",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/forum/list"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/forum/logistics",
    "title": "6. 后勤人员list",
    "version": "0.0.1",
    "name": "forum_logistics",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "forumId",
            "description": "<p>会议id</p>"
          }
        ]
      }
    },
    "group": "forum",
    "description": "<p>返回后勤人员信息列表</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/ForumControll.java",
    "groupTitle": "forum",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/forum/logistics"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/forum/permissions",
    "title": "9.用户权限",
    "version": "0.0.1",
    "name": "forum_permissions",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "forumId",
            "description": "<p>会议id</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "agendaId",
            "description": "<p>议程id(暂时不传) 用来三级权限</p>"
          }
        ]
      }
    },
    "group": "forum",
    "description": "<p>返回权限简称结果</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/ForumControll.java",
    "groupTitle": "forum",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/forum/permissions"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/forum/promoter/{id}",
    "title": "3.会议举办者列表(联席主席)",
    "version": "0.0.1",
    "name": "forum_promoter",
    "group": "forum",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>名称</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "description",
            "description": "<p>简介</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "imgUrl",
            "description": "<p>头像</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\"status\": 200,\n\"msg\": \"\",\n\"data\": [\n{\n\"id\": 45,\n\"name\": \"华佗\",\n\"description\": null,\n\"imgUrl\": null\n},\n{\n\"id\": 67,\n\"name\": \"111\",\n\"description\": null,\n\"imgUrl\": null\n},\n{\n\"id\": 78,\n\"name\": \"111\",\n\"description\": null,\n\"imgUrl\": null\n},\n{\n\"id\": 75,\n\"name\": \"111\",\n\"description\": null,\n\"imgUrl\": null\n}\n]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/ForumControll.java",
    "groupTitle": "forum",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/forum/promoter/{id}"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/forum/questionnaire",
    "title": "5.调查问卷url",
    "version": "0.0.1",
    "name": "forum_questionnaire",
    "group": "forum",
    "description": "<p>返回调查问卷的url</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/ForumControll.java",
    "groupTitle": "forum",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/forum/questionnaire"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/forum/sponsors",
    "title": "7.赞助商list",
    "version": "0.0.1",
    "name": "forum_sponsors",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "forumId",
            "description": "<p>会议id</p>"
          }
        ]
      }
    },
    "group": "forum",
    "description": "<p>返回赞助商列表</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/ForumControll.java",
    "groupTitle": "forum",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/forum/sponsors"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/forum/sponsors/{id}",
    "title": "8.赞助商详情",
    "version": "0.0.1",
    "name": "forum_sponsors_id",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "id",
            "description": "<p>供应商id</p>"
          }
        ]
      }
    },
    "group": "forum",
    "description": "<p>返回赞助商详情</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/ForumControll.java",
    "groupTitle": "forum",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/forum/sponsors/{id}"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/forum/venues",
    "title": "10.场馆接口",
    "version": "0.0.1",
    "name": "forum_venues",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "forumId",
            "description": "<p>会议id</p>"
          }
        ]
      }
    },
    "group": "forum",
    "description": "<p>返回场馆列表</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/ForumControll.java",
    "groupTitle": "forum",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/forum/venues"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/user/findPassword",
    "title": "13.找回密码",
    "version": "0.0.1",
    "name": "user_findPassword",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "email",
            "description": "<p>用户邮箱</p>"
          }
        ]
      }
    },
    "group": "user",
    "description": "<p>findPassword</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "账号不存在或者其他错误信息:\n{\n\"status\":500,\n\"data\":{},\n\"msg\":\"邮箱账号不存在.\"\n}\n找回密码邮件发送成功:\n{\n\"status\":200,\n\"data\":{},\n\"msg\":\"邮件发送成功\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/UserController.java",
    "groupTitle": "user",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/user/findPassword"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/user/fllowlist",
    "title": "12.获取关注我的用户列表",
    "version": "0.0.1",
    "name": "user_fllowlist",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id</p>"
          }
        ]
      }
    },
    "group": "user",
    "description": "<p>fllowlist</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/UserController.java",
    "groupTitle": "user",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/user/fllowlist"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/user/head/upload",
    "title": "4.上传头像",
    "version": "0.0.1",
    "name": "user_head_upload",
    "group": "user",
    "description": "<p>上传头像</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "userId",
            "description": "<p>用户ID</p>"
          },
          {
            "group": "Parameter",
            "type": "FILE",
            "optional": false,
            "field": "file",
            "description": "<p>图片文件</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\"status\": 200,\n\"msg\": \"\",\n\"data\": \"http://localhost:8080/files/upload/img/2016/8/558537507501307.jpg\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/UserController.java",
    "groupTitle": "user",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/user/head/upload"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/user/info",
    "title": "5.查询用户信息",
    "version": "0.0.1",
    "name": "user_info",
    "group": "user",
    "description": "<p>修改用户信息</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "userId",
            "description": "<p>用户ID</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\"status\": 200,\n\"msg\": \"\",\n\"data\": {\n      \"user\": {\n      \"id\": 3,\n      \"name\": \"王彬\",\n      \"imgUrl\": \"http://localhost:8080/files/upload/img/2016/8/558537507501307.jpg\",\n      \"password\": \"123456\",\n      \"ename\": \"wangbin\",\n      \"nationality\": \"中国\",\n      \"sex\": \"男\",\n      \"tel\": \"18801285391\",\n      \"hospital\": \"人民医院\",\n      \"post\": \"外科医生\",\n      \"titles\": \"专家\",\n      \"department\": \"外科\",\n      \"email\": \"bur@13.com\",\n      \"createDate\": \"2016-08-24 10:13:23\",\n      \"remark\": \"\"\n      }\n     }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/UserController.java",
    "groupTitle": "user",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/user/info"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/user/list",
    "title": "10.获取普通注册用户列表",
    "version": "0.0.1",
    "name": "user_list",
    "group": "user",
    "description": "<p>list</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/UserController.java",
    "groupTitle": "user",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/user/list"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/user/login",
    "title": "2.APP用户登陆",
    "version": "0.0.1",
    "name": "user_login",
    "group": "user",
    "description": "<p>用户登录</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "username",
            "description": "<p>邮箱/手机号</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "password",
            "description": "<p>密码</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\"status\": 200,\n\"msg\": \"\",\n\"data\": {\n      \"user\": {\n      \"id\": 3,\n      \"name\": \"王彬\",\n      \"imgUrl\": \"http://localhost:8080/files/upload/img/2016/8/558537507501307.jpg\",\n      \"password\": \"123456\",\n      \"ename\": \"wangbin\",\n      \"nationality\": \"中国\",\n      \"sex\": \"男\",\n      \"tel\": \"18801285391\",\n      \"hospital\": \"人民医院\",\n      \"post\": \"外科医生\",\n      \"titles\": \"专家\",\n      \"department\": \"外科\",\n      \"email\": \"bur@13.com\",\n      \"createDate\": \"2016-08-24 10:13:23\",\n      \"remark\": \"\"\n      }\n     }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/UserController.java",
    "groupTitle": "user",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/user/login"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/user/register",
    "title": "1.APP用户注册",
    "version": "0.0.1",
    "name": "user_register",
    "group": "user",
    "description": "<p>用户注册</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "email",
            "description": "<p>邮箱</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "password",
            "description": "<p>密码</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "name",
            "description": "<p>名称</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "ename",
            "description": "<p>英文名</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "nationality",
            "description": "<p>国籍</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "sex",
            "description": "<p>性别</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "tel",
            "description": "<p>手机</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "hospital",
            "description": "<p>医院</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "post",
            "description": "<p>职务</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "titles",
            "description": "<p>职称</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "department",
            "description": "<p>科室</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "remark",
            "description": "<p>备注</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\"status\": 200,\n\"msg\": \"\",\n\"data\": {\n      \"user\": {\n      \"id\": 3,\n      \"name\": \"王彬\",\n      \"imgUrl\": \"http://localhost:8080/files/upload/img/2016/8/558537507501307.jpg\",\n      \"password\": \"123456\",\n      \"ename\": \"wangbin\",\n      \"nationality\": \"中国\",\n      \"sex\": \"男\",\n      \"tel\": \"18801285391\",\n      \"hospital\": \"人民医院\",\n      \"post\": \"外科医生\",\n      \"titles\": \"专家\",\n      \"department\": \"外科\",\n      \"email\": \"bur@13.com\",\n      \"createDate\": \"2016-08-24 10:13:23\",\n      \"remark\": \"\"\n      }\n     }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/UserController.java",
    "groupTitle": "user",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/user/register"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/user/travelInformation",
    "title": "6.保存接机信息",
    "version": "0.0.1",
    "name": "user_travelInformation",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "id",
            "description": "<p>接机信息id(新增不填, 修改才有值)</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "forumId",
            "description": "<p>会议id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "inboundDate",
            "description": "<p>来程日期</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "inboundMode",
            "description": "<p>来程方式</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "flightNumber",
            "description": "<p>航班号</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "flightDate",
            "description": "<p>飞机降落时间</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "trainTrips",
            "description": "<p>列车车次</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "trainDate",
            "description": "<p>列车开车时间</p>"
          }
        ]
      }
    },
    "group": "user",
    "description": "<p>保存接机信息</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/UserController.java",
    "groupTitle": "user",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/user/travelInformation"
      }
    ]
  },
  {
    "type": "get",
    "url": "/api/user/travelInformation",
    "title": "7.获取接机信息",
    "version": "0.0.1",
    "name": "user_travelInformation_get_",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "forumId",
            "description": "<p>会议id</p>"
          }
        ]
      }
    },
    "group": "user",
    "description": "<p>获取接机信息</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/UserController.java",
    "groupTitle": "user",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/user/travelInformation"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/user/unwatch",
    "title": "9.取消关注用户",
    "version": "0.0.1",
    "name": "user_unwatch",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "fllowUserId",
            "description": "<p>关注的用户id</p>"
          }
        ]
      }
    },
    "group": "user",
    "description": "<p>unwatch</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/UserController.java",
    "groupTitle": "user",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/user/unwatch"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/user/update",
    "title": "3.修改用户信息",
    "version": "0.0.1",
    "name": "user_update",
    "group": "user",
    "description": "<p>修改用户信息</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "email",
            "description": "<p>邮箱</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "password",
            "description": "<p>密码</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "name",
            "description": "<p>名称</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "ename",
            "description": "<p>英文名</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "nationality",
            "description": "<p>国籍</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "sex",
            "description": "<p>性别</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "tel",
            "description": "<p>手机</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "hospital",
            "description": "<p>医院</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "post",
            "description": "<p>职务</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "titles",
            "description": "<p>职称</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "department",
            "description": "<p>科室</p>"
          },
          {
            "group": "Parameter",
            "type": "STRING",
            "optional": false,
            "field": "remark",
            "description": "<p>备注</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\"status\": 200,\n\"msg\": \"\",\n\"data\": {\n      \"user\": {\n      \"id\": 3,\n      \"name\": \"王彬\",\n      \"imgUrl\": \"http://localhost:8080/files/upload/img/2016/8/558537507501307.jpg\",\n      \"password\": \"123456\",\n      \"ename\": \"wangbin\",\n      \"nationality\": \"中国\",\n      \"sex\": \"男\",\n      \"tel\": \"18801285391\",\n      \"hospital\": \"人民医院\",\n      \"post\": \"外科医生\",\n      \"titles\": \"专家\",\n      \"department\": \"外科\",\n      \"email\": \"bur@13.com\",\n      \"createDate\": \"2016-08-24 10:13:23\",\n      \"remark\": \"\"\n      }\n     }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/UserController.java",
    "groupTitle": "user",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/user/update"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/user/watch",
    "title": "8.关注用户",
    "version": "0.0.1",
    "name": "user_watch",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id</p>"
          },
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "fllowUserId",
            "description": "<p>关注的用户id</p>"
          }
        ]
      }
    },
    "group": "user",
    "description": "<p>watch</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/UserController.java",
    "groupTitle": "user",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/user/watch"
      }
    ]
  },
  {
    "type": "post",
    "url": "/api/user/watchlist",
    "title": "11.获取关注的用户列表",
    "version": "0.0.1",
    "name": "user_watchlist",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "INT",
            "optional": false,
            "field": "userId",
            "description": "<p>用户id</p>"
          }
        ]
      }
    },
    "group": "user",
    "description": "<p>watchlist</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Result",
            "optional": false,
            "field": "Result",
            "description": "<p>返回结果</p>"
          }
        ]
      }
    },
    "filename": "src/main/java/me/binf/ivf/controller/api/UserController.java",
    "groupTitle": "user",
    "sampleRequest": [
      {
        "url": "http://14.17.81.101:8080/ivf/api/user/watchlist"
      }
    ]
  }
] });
