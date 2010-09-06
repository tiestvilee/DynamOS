rm -R org/dynamos/image/analysis
rm -R org/dynamos/image/lexer
rm -R org/dynamos/image/node
rm -R org/dynamos/image/parser
java -jar ../../sablecc/sablecc-3.2/lib/sablecc.jar image_grammar.sablecc
