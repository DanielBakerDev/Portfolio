class Process:

    def read_in_document(self):
        f = open("EmailLog.txt", "r", encoding='utf-8')

        word_list = []
        string = ""
        
        for line in f:
            string += line
            if line == "<<End>>\n":
                word_list.append(string)
                string = ""
            elif line == "<<End>>":
                word_list.append(string)
                string = ""               
        return word_list

    def convertToFormat(self, token_list):
        word_list = []
        sentence = ""
        entire_total = 0
        for token in token_list:
            sentence = ""
            sentence += token.email + " : "

            total = self.getTotal(token.money)

            entire_total += total

            sentence += self.formatNouns(token.noun, token.money)

            word_list.append(sentence)

        word_list.append('Total Requests: ' + self.formatForString(entire_total))
        return word_list
    
    def getTotal(self, money_list):
        price = 0.0
        for money in money_list:
            price += float(self.removeComma(money))
        return price

        
    def removeComma(self, number):
        num = number.replace(',', '')
        return num

    def formatForString(self, number):
        number = int(number)
        num = '$'+ str("{:,}".format(number))
        return num
    
    def formatFinaltring(self, number):
        num = number.replace(',', '')
        num = int(num)
        num = '$'+ str("{:,}".format(num))
        return num

    def formatNouns(self, noun_list, money_list):
        line = ""
        if len(noun_list) == 1:
            line = self.formatFinaltring(money_list[0]) + " to " + noun_list[0]
        elif len(noun_list) == 2:
            line = self.formatFinaltring(money_list[0]) + " to " + noun_list[0] + " and " + self.formatFinaltring(money_list[1]) + " to " + noun_list[1]
        elif len(noun_list) > 2:
            count = len(noun_list)
            i = 0
            for noun in noun_list:
                i += 1
                if i == count:
                    line += 'and ' + self.formatFinaltring(money_list[i-1]) + ' to ' + noun
                else:
                    line += self.formatFinaltring(money_list[i-1]) + ' to ' + noun + ", "
        return line

    
    def SubsttituteWordForNum(self, num_list):
        substitutes = ('one', 'hundred','thousand','million','billion')
        i = 0

        for x in num_list:
            if x == 'one':
                num_list.remove(x)
            elif x == 'hundred':
                num_list[i-1] = num_list[i-1] + "00"
                num_list.remove(x)
            elif x == 'thousand':
                num_list[i-1] = num_list[i-1] + "000"
                num_list.remove(x)
            elif x == 'million':
                num_list[i-1] = num_list[i-1] + "000000"
                num_list.remove(x)
            elif x == 'billion':
                num_list[i-1] = num_list[i-1] + "000000000"
                num_list.remove(x)
            i += 1
        return num_list