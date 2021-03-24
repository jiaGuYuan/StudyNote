# json数据格式调整

## json dict串中value数据包含双引号
eg: {..,"attribute_value":"23"24inch",..}
```
import re
# 属性的value中存在双引号,需要转义; 如 {..,"attribute_value":"23"24inch",..}
# r'(?::\s*")(.*?".*?)(?:"\s*[,}])'匹配attr json dict串中包含双引号的value

attr = '{"attribute_value_id":0,"attribute_value":"23"24inch","attribute_id":"0.5""}'
print(attr)
semicolons = re.compile(r'(?::\s*")(.*?".*?)(?:"\s*[,}])') # re.MULTILINE
include_quote_values = semicolons.findall(attr)
print(include_quote_values)
for quote_str in include_quote_values:
    attr = attr.replace(quote_str, quote_str.replace(r'"', r'\"'))
    print(attr)
    
print('结果:', attr)
```
