function value = map(x, in_min, in_max, out_min, out_max)
value = (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
end

