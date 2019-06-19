create table user_subscriptions(
  subscriber_id int8 not NULL REFERENCES usr,
  channel_id int8 not NULL REFERENCES usr,
  PRIMARY KEY (subscriber_id, channel_id)
);