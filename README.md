# StackCardsAndroid
Android Stack Cards swipe able

RelativeLayout cardsContainer = (RelativeLayout)findViewById(R.id.relative_container);

//Activity owningActivity, RelativeLayout container, int cardHeightDP, int cardDiffDP,float cardScale, int animationDuration)
StackCards stackCards = new StackCards(MainActivity.this, cardsContainer, 100, 50, (float) 0.2, 500);

//(Number of Cards, The Layout of the Card)
stackCards.initCards(7, R.layout.stack_card);