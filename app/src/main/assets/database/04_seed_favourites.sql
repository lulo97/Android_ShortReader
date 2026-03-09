-- First, insert word details
INSERT OR IGNORE INTO word_details (word, meaning, example, part_of_speech, pronunciation) VALUES
-- Words from Alice in Wonderland
('curious', 'Eager to know or learn something', 'Alice was curious about the rabbit hole.', 'adjective', '/ˈkjʊə.ri.əs/'),
('rabbit', 'A small burrowing mammal with long ears', 'The white rabbit checked his pocket watch.', 'noun', '/ˈræb.ɪt/'),
('wonderland', 'An imaginary land of marvels or wonders', 'Alice fell down the rabbit hole into Wonderland.', 'noun', '/ˈwʌn.də.lænd/'),
('hatter', 'A person who makes and sells hats', 'The Mad Hatter hosted a tea party.', 'noun', '/ˈhæt.ər/'),
('caterpillar', 'The larval stage of a butterfly or moth', 'The caterpillar sat on a mushroom.', 'noun', '/ˈkæt.ə.pɪl.ər/'),
('cheshire', 'Referring to the Cheshire Cat', 'The Cheshire Cat grinned mysteriously.', 'adjective', '/ˈtʃeʃ.ər/'),
('grin', 'To smile broadly', 'The cat continued to grin.', 'verb', '/ɡrɪn/'),
('disappear', 'To cease to be visible', 'The cat slowly disappeared leaving only its grin.', 'verb', '/ˌdɪs.əˈpɪər/'),

-- Words from Sherlock Holmes
('deduction', 'The inference of particular instances by reference to a general law', 'Holmes used deduction to solve the crime.', 'noun', '/dɪˈdʌk.ʃən/'),
('elementary', 'Relating to the simplest principles', '"Elementary, my dear Watson," said Holmes.', 'adjective', '/ˌel.ɪˈmen.tər.i/'),
('mysterious', 'Difficult or impossible to understand', 'The mysterious stranger left no clues.', 'adjective', '/mɪˈstɪə.ri.əs/'),
('investigation', 'A formal inquiry or systematic study', 'The investigation revealed new evidence.', 'noun', '/ɪnˌves.tɪˈɡeɪ.ʃən/'),
('clue', 'A piece of evidence or information', 'The detective found an important clue.', 'noun', '/kluː/'),
('evidence', 'Information that supports a conclusion', 'The evidence pointed to the butler.', 'noun', '/ˈev.ɪ.dəns/'),
('remarkable', 'Worthy of attention; striking', 'She had a remarkable memory.', 'adjective', '/rɪˈmɑː.kə.bəl/'),
('observe', 'To notice or perceive something', 'Holmes observed the smallest details.', 'verb', '/əbˈzɜːv/'),

-- Common English words
('adventure', 'An unusual and exciting experience', 'Their journey was a great adventure.', 'noun', '/ədˈven.tʃər/'),
('mystery', 'Something that is difficult to understand', 'The old house held many mysteries.', 'noun', '/ˈmɪs.tər.i/'),
('treasure', 'A collection of precious things', 'They searched for buried treasure.', 'noun', '/ˈtreʒ.ər/'),
('journey', 'An act of traveling from one place to another', 'The journey took three days.', 'noun', '/ˈdʒɜː.ni/'),
('ancient', 'Very old; from a long time ago', 'They discovered ancient ruins.', 'adjective', '/ˈeɪn.ʃənt/'),
('legend', 'A traditional story regarded as historical', 'The legend of King Arthur is famous.', 'noun', '/ˈledʒ.ənd/'),
('brilliant', 'Very bright or intelligent', 'She had a brilliant idea.', 'adjective', '/ˈbrɪl.jənt/'),
('curiosity', 'A strong desire to know or learn something', 'Her curiosity led her to explore.', 'noun', '/ˌkjʊə.riˈɒs.ə.ti/'),

-- Basic/common English words you requested
('to', 'Expressing motion in the direction of', 'I am going to the store.', 'preposition', '/tuː/'),
('get', 'To come to have or hold; receive', 'I need to get some milk.', 'verb', '/ɡet/'),
('for', 'Used to indicate the purpose of an object or action', 'This gift is for you.', 'preposition', '/fɔːr/'),
('by', 'Indicating the means of achieving something', 'She traveled by train.', 'preposition', '/baɪ/'),
('sitting', 'In a position of rest on a seat', 'She was sitting on the bench.', 'verb', '/ˈsɪt.ɪŋ/'),
('from', 'Indicating the point at which a journey starts', 'I come from London.', 'preposition', '/frʌm/'),
('the', 'Denoting one or more people or things already mentioned', 'The book is on the table.', 'article', '/ðə/'),
('and', 'Used to connect words of the same part of speech', 'Apples and oranges are fruits.', 'conjunction', '/ænd/'),
('with', 'Accompanied by', 'She came with her friend.', 'preposition', '/wɪð/'),
('at', 'Expressing location or time', 'We will meet at the park.', 'preposition', '/æt/'),
('on', 'Physically in contact with and supported by', 'The book is on the table.', 'preposition', '/ɒn/'),
('as', 'Used to indicate the function or character of', 'He works as a teacher.', 'adverb', '/æz/'),
('but', 'Used to introduce a phrase contrasting with what has been mentioned', 'She is small but strong.', 'conjunction', '/bʌt/'),
('or', 'Used to link alternatives', 'Tea or coffee?', 'conjunction', '/ɔːr/'),
('an', 'The form of "a" used before vowel sounds', 'She ate an apple.', 'article', '/æn/'),
('have', 'Possess, own, or hold', 'I have two dogs.', 'verb', '/hæv/'),
('be', 'Exist; be present', 'I want to be a doctor.', 'verb', '/biː/'),
('this', 'Used to identify a specific person or thing close at hand', 'This is my house.', 'determiner', '/ðɪs/'),
('that', 'Used to identify a specific person or thing observed', 'That car is fast.', 'determiner', '/ðæt/'),
('it', 'Used to refer to a thing previously mentioned', 'The book - have you read it?', 'pronoun', '/ɪt/');

-- Then, add some words to favourites (referencing word_details)
INSERT OR IGNORE INTO favourite_words (word_detail_id, notes) VALUES
((SELECT id FROM word_details WHERE word = 'curious'), 'My favorite word from Alice'),
((SELECT id FROM word_details WHERE word = 'rabbit'), 'The White Rabbit character'),
((SELECT id FROM word_details WHERE word = 'deduction'), 'Key to solving mysteries'),
((SELECT id FROM word_details WHERE word = 'mysterious'), 'Great for describing stories'),
((SELECT id FROM word_details WHERE word = 'adventure'), 'Perfect for travel'),
((SELECT id FROM word_details WHERE word = 'ancient'), 'Useful for history topics'),
((SELECT id FROM word_details WHERE word = 'brilliant'), 'Positive adjective'),
((SELECT id FROM word_details WHERE word = 'get'), 'Most common verb'),
((SELECT id FROM word_details WHERE word = 'sitting'), 'From Alice in Wonderland');