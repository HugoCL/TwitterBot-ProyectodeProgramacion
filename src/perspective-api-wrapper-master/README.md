# perspective-api-wrapper
An easy to use wrapper around the Google Perspective API

```
PerspectiveAPI api = new PerspectiveAPIBuilder()
  .setApiKey("IAmAKey")
  .build();

ListenableFuture<AnalyzeCommentResponse> future = api.analyze()
  .setComment("What kind of stupid name is Guy?")
  .addContext("Hey everyone! I'm Guy, the author of this work")
  .addAttribute(Attribute.ofType(Attribute.TOXICITY))
  .postAsync();

AnalyzeCommentResponse response = future.get();

float summaryScore = response.getAttributeSummaryScore(Attribute.TOXICITY);
```
