package be.allersma.boadskipje;

public class EAN13CodeBuilder {
    private final String codeStringValue;
    private String generatedCode;

    public EAN13CodeBuilder(String codeString) {
        codeStringValue = codeString;
        parse();
    }

    public String getCode() {
        return generatedCode;

    }

    // this method generates EAN 13 control number and returns full string to encode
    private String getFullCode() {

        int chetVal = 0, nechetVal = 0;
        String codeToParse = codeStringValue;

        for( int index = 0;index<6;index++ )
        {
            chetVal += Integer.parseInt(codeToParse.substring(
                    index * 2 + 1, index * 2 + 2));
            nechetVal += Integer.parseInt(codeToParse.substring(
                    index * 2, index * 2 + 1));
        }

        chetVal *= 3;
        int controlNumber = 10 - (chetVal+nechetVal)%10;
        if( controlNumber == 10 ) controlNumber  = 0;

        codeToParse += String.valueOf(controlNumber);

        return codeToParse;

    }

    private String DigitToUpperCase(String digit) {
        String letters  = "ABCDEFGHIJ";
        int position = Integer.parseInt(digit);

        return letters.substring(position,position+1);

    }

    private String DigitToLowerCase(String digit) {
        String letters  = "abcdefghij";
        int position = Integer.parseInt(digit);

        return letters.substring(position,position+1);

    }

    // this method generates EAN 13 encoded string
    // algorithm can be found at http://en.wikipedia.org/wiki/EAN-13
    private String createEAN13Code(String rawCode) {
        int firstFlag = Integer.parseInt(rawCode.substring(0, 1));

        String leftString = rawCode.substring(1,7);
        String rightString = rawCode.substring(7);

        String rightCode = "";
        String leftCode = "";

        for( int i=0;i<6;i++)
        {
            rightCode += DigitToLowerCase( rightString.substring(i,i+1) );
        }



        if( firstFlag == 0 )
        {
            leftCode = "#!"+leftString.charAt(0)
                    +leftString.charAt(1)
                    +leftString.charAt(2)
                    +leftString.charAt(3)
                    +leftString.charAt(4)
                    +leftString.substring(5);
        }
        if( firstFlag == 1 )
        {

            leftCode = "$!"+leftString.charAt(0)
                    +leftString.charAt(1)
                    +DigitToUpperCase(leftString.substring(2,3))
                    +leftString.charAt(3)
                    +DigitToUpperCase(leftString.substring(4,5))
                    +DigitToUpperCase(leftString.substring(5));
        }
        if( firstFlag == 2 )
        {
            leftCode = "%!"+leftString.charAt(0)
                    +leftString.charAt(1)
                    +DigitToUpperCase(leftString.substring(2,3))
                    +DigitToUpperCase(leftString.substring(3,4))
                    +leftString.charAt(4)
                    +DigitToUpperCase(leftString.substring(5));
        }
        if( firstFlag == 3 )
        {
            leftCode = "&!"+leftString.charAt(0)
                    +leftString.charAt(1)
                    +DigitToUpperCase(leftString.substring(2,3))
                    +DigitToUpperCase(leftString.substring(3,4))
                    +DigitToUpperCase(leftString.substring(4,5))
                    +leftString.substring(5);
        }
        if( firstFlag == 4 )
        {
            leftCode = "'!"+leftString.charAt(0)
                    +DigitToUpperCase(leftString.substring(1,2))
                    +leftString.charAt(2)
                    +leftString.charAt(3)
                    +DigitToUpperCase(leftString.substring(4,5))
                    +DigitToUpperCase(leftString.substring(5));
        }
        if( firstFlag == 5 )
        {
            leftCode = "(!"+leftString.charAt(0)
                    +DigitToUpperCase(leftString.substring(1,2))
                    +DigitToUpperCase(leftString.substring(2,3))
                    +leftString.charAt(3)
                    +leftString.charAt(4)
                    +DigitToUpperCase(leftString.substring(5));
        }
        if( firstFlag == 6 )
        {
            leftCode = ")!"+leftString.charAt(0)
                    +DigitToUpperCase(leftString.substring(1,2))
                    +DigitToUpperCase(leftString.substring(2,3))
                    +DigitToUpperCase(leftString.substring(3,4))
                    +leftString.charAt(4)
                    +leftString.substring(5);
        }
        if( firstFlag == 7 )
        {
            leftCode = "*!"+leftString.charAt(0)
                    +DigitToUpperCase(leftString.substring(1,2))
                    +leftString.charAt(2)
                    +DigitToUpperCase(leftString.substring(3,4))
                    +leftString.charAt(4)
                    +DigitToUpperCase(leftString.substring(5));
        }
        if( firstFlag == 8 )
        {
            leftCode = "+!"+leftString.charAt(0)
                    +DigitToUpperCase(leftString.substring(1,2))
                    +leftString.charAt(2)
                    +DigitToUpperCase(leftString.substring(3,4))
                    +DigitToUpperCase(leftString.substring(4,5))
                    +leftString.substring(5);
        }
        if( firstFlag == 9 )
        {
            leftCode = ",!"+leftString.charAt(0)
                    +DigitToUpperCase(leftString.substring(1,2))
                    +DigitToUpperCase(leftString.substring(2,3))
                    +leftString.charAt(3)
                    +DigitToUpperCase(leftString.substring(4,5))
                    +leftString.substring(5);
        }


        return leftCode + "-" + rightCode + "!";
    }

    private void parse() {
        String fullString = getFullCode();
        System.out.println( "Full code: " + fullString );

        generatedCode = createEAN13Code(fullString);

        System.out.println( "Generated code: " + generatedCode );

    }
}
