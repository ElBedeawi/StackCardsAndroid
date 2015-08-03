# StackCardsAndroid
Android Stack Cards swipe able

<code>
RelativeLayout cardsContainer = (RelativeLayout)findViewById(R.id.relative_container);
</code>

<i>//Activity owningActivity, RelativeLayout container, int cardHeightDP, int cardDiffDP,float cardScale, int animationDuration)</i>

<code>
StackCards stackCards = new StackCards(MainActivity.this, cardsContainer, 100, 50, (float) 0.2, 500);
</code>

<i>//(Number of Cards, The Layout of the Card)</i>

<code>
stackCards.initCards(7, R.layout.stack_card);
</code>