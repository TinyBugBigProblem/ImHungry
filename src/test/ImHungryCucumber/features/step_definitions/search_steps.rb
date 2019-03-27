Given(/^I am on the ImHungry Search Page$/) do
  visit "http://localhost:8080/FeedMe/jsp/search.jsp"
end

Then(/^there is a searchTerm input field$/) do
  page.should have_selector("input[type=text][id=userInput][placeholder='Enter Food']")
end


Then(/^there is a searchNumber input field$/) do
  page.should have_selector("input[type=text][id=searchTermTest]")
end

Then(/^there is a default value 5$/) do
  expect(page.find_by_id("searchTermTest").value).to eq "5"
end

Then(/^there is a Feed Me button$/) do
  page.should have_selector("button[id=feedMeButton]")
end

Then(/^there is a red Feed Me button$/) do
  page.should have_selector("button[id=feedMeButton][style='color: red;']")
end

Then(/^there is a Emoji Image$/) do
  page.should have_selector("img[id=emoji]")
end

Then(/^there is a grumpy Emoji Image$/) do
  page.should have_selector("img[src='https://images.emojiterra.com/twitter/v11/512px/1f620.png']")
end

Then(/^there is a happy Emoji Image$/) do
  page.should have_selector("img[src='https://buildahead.com/wp-content/uploads/2017/02/happy-emoji-smaller.png']")

end


When(/^I enter "([^"]*)" in the search box$/) do |searchArg|
  fill_in('q', :with => searchArg)
end

When(/^I enter "([^"]*)" in the search number box$/) do |searchNumArg|
  fill_in('n', :with => searchNumArg)
end

When(/^press search$/) do
  page.find_by_id("feedMeButton").click()
end

Then(/^I should see results for "([^"]*)"$/) do |arg1|
  expect(page.find_by_id("titleHeader").native.text).to eq "Results For " + arg1
end

Then(/^there is a Feed Me! button$/) do
  expect(page.find_by_id("feedMeButton").native.text).to eq "Feed Me!"
end

Then(/^the title of search page is I'm Hungry$/) do
  page.should have_content("I'm Hungry")
end


