package org.jenkinsci.plugins

//stolen from here
//http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Groovy

class Levenshtien {
    static int distance(String str1, String str2) {
        int str1Len = str1.length()
        int  str2Len = str2.length()
        int[][] distance = new int[str1Len + 1][str2Len + 1]
        (str1Len + 1).times { distance[it][0] = it }
        (str2Len + 1).times { distance[0][it] = it }
        (1..str1Len).each { i ->
            (1..str2Len).each { j ->
                distance[i][j] = [distance[i - 1][j] + 1, distance[i][j - 1] + 1, str1[i - 1] ==
                        str2[j - 1] ? distance[i - 1][j - 1] : distance[i - 1][j - 1] + 1].min()
            }
        }
        distance[str1Len][str2Len]
    }
}
