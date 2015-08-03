# StackCardsAndroid
Android Stack Cards swipe able

<code>
RelativeLayout cardsContainer = (RelativeLayout)findViewById(R.id.relative_container);

<i>//Activity owningActivity, RelativeLayout container, int cardHeightDP, int cardDiffDP,float cardScale, int animationDuration)</i>

StackCards stackCards = new StackCards(MainActivity.this, cardsContainer, 100, 50, (float) 0.2, 500);

<i>//(Number of Cards, The Layout of the Card)</i>
stackCards.initCards(7, R.layout.stack_card);
</code>