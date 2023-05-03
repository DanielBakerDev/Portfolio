import torch
import tensorflow as tf
from transformers import AutoTokenizer, GPT2LMHeadModel
tokenizer = AutoTokenizer.from_pretrained("gpt2")
# add the EOS token as PAD token to avoid warnings
model = GPT2LMHeadModel.from_pretrained("gpt2", pad_token_id=tokenizer.eos_token_id)

torch.manual_seed(0)

#1) be at least 100 words long; 2) not have repeated phrases; and 3) be the same every time your script is run
print("My GPT-2 Story:")
print("---------------")

generated_text = model.generate(
    pad_token_id=tokenizer.eos_token_id,
    do_sample=True,
    max_length=200,
    repetition_penalty=1.0,
    num_return_sequences=1
)[0]

generated_text = tokenizer.decode(generated_text, skip_special_tokens=True)
print(generated_text)