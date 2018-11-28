package edu.hubu.wdpt.service;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * created by Sugar  2018/11/16 19:26
 * 用于问题发布时对其中的敏感词进行过滤
 */
@Service
public class SensitiveService implements InitializingBean {

    private static final  Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    /**
     * 默认敏感词替换符
     */
    private static final String DEFAULT_REPLACEMENT = "***";

    @Override
    public void afterPropertiesSet() throws Exception {
        rootNode = new TrieNode();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("sensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
                addWord(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }

    }

    /**
     * 判断是否是一个符号
     */
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    private void addWord(String lineTxt){
        TrieNode tempNode = rootNode;
        // 循环每个字节
        for (int i = 0; i < lineTxt.length(); ++i) {
            Character c = lineTxt.charAt(i);
            // 过滤空格
            if (isSymbol(c)) {
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);

            if (node == null) { // 没初始化
                node = new TrieNode();
                tempNode.setSubNode(c, node);
            }

            tempNode = node;

            if (i == lineTxt.length() - 1) {
                // 关键词结束， 设置结束标志
                tempNode.setKeyWordEnd(true);
            }
        }
    }
     private class TrieNode{
         //是否是关键字的结尾
         private boolean end = false;

         private Map<Character,TrieNode> subNodes = new HashMap<Character, TrieNode>();

         public void setSubNode(Character key,TrieNode trieNode){
             subNodes.put(key,trieNode);
         }

         public TrieNode getSubNode(Character key){
             return subNodes.get(key);
         }

         boolean isKeyWordEnd(){
             return  end;
         }

         void setKeyWordEnd(boolean end){
             this.end = end;
         }
    }
    //根节点
    private TrieNode rootNode = new TrieNode();


    /**
     * 过滤敏感词
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        String replacement = DEFAULT_REPLACEMENT;
        StringBuilder result = new StringBuilder();

        TrieNode tempNode = rootNode;
        int begin = 0; // 回滚数
        int position = 0; // 当前比较的位置

        while (position < text.length()) {
            char c = text.charAt(position);
            // 空格直接跳过
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);

            // 当前位置的匹配结束
            if (tempNode == null) {
                // 以begin开始的字符串不存在敏感词
                result.append(text.charAt(begin));
                // 跳到下一个字符开始测试
                position = begin + 1;
                begin = position;
                // 回到树初始节点
                tempNode = rootNode;
            } else if (tempNode.isKeyWordEnd()) {
                // 发现敏感词， 从begin到position的位置用replacement替换掉
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }
        result.append(text.substring(begin));
        return result.toString();
    }


//    public static void main(String[] args) {
//        SensitiveService s = new SensitiveService();
//        s.addWord("色情");
//        s.addWord("好色");
//        System.out.print(s.filter("你好X色**情XX"));
//    }
}
