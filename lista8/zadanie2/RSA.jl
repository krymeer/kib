using Primes

function generateTwoPrimeNumbers()
  a = 2^15
  b = 2^16
  p = 0
  q = 0

  while(!(isprime(p)))
    p = rand(a:b)
  end

  while(!(isprime(q)) || p == q)
    q = rand(a:b)
  end

  return p, q
end

function EulerPhi(p::Int64, q::Int64)
  return (p-1)*(q-1)
end

function EuclideanAlgorithm(a::Int64, b::Int64)
  while b != 0
    c = a % b
    a = b
    b = c
  end  
  return a
end

function getE(n::Int64)
  e = 3
  while EuclideanAlgorithm(e, n) > 1
    e += 2
  end
  return e
end

function getD(a::Int64, b::Int64)
  a0 = a; b0 = b
  p = 1; q = 0; r = 0; s = 1

  while b != 0
    c = a%b
    q = div(a, b)
    a = b
    b = c
    nr = p - q*r 
    ns = q - q*s
    p = r; q = s
    r = nr; s = ns
  end

  # NWD(a, b) = a0*p + b0*q
  if (p < 0)
    p += b0
  end

  return p
end

function hash(k::Int64)
  return k % 97
end

function exponentationModuloN(a::Int64, k::Int64, n::Int64)
  result = 1
  while k > 0
    if k%2 == 1
      result = (result*a) % n 
    end
    a = (a*a) % n
    k = div(k, 2)
  end
  return result
end

function generateKey(crt = false)
  p, q = generateTwoPrimeNumbers();
  n = p*q
  phi = EulerPhi(p, q)
  e = getE(phi)
  d = getD(e, phi)
  if crt == true
    dp = d % (p-1)
    dq = d % (q-1)
    return [e, n], [dp, dq, p, q, n]
  else
    return [e, n], [d, n]
  end

end

function encrypt(m::Int64, arr::Array{Int64})
  e = arr[1]
  n = arr[2]
  return exponentationModuloN(m, e, n)
end

function decrypt(c::Int64, arr::Array{Int64})
  d = arr[1]
  n = arr[2]
  return exponentationModuloN(c, d, n)
end

function decryptWithCRT(c:: Int64, arr::Array{Int64})
  dp = arr[1]
  dq = arr[2]
  p = arr[3]
  q = arr[4]
  n = arr[5]
  expP = exponentationModuloN(c, dp, p)
  expQ = exponentationModuloN(c, dq, q)
  if expP == expQ
    return expP
  else
    return -666
  end   
end

function message()
  m = 108
  println("\nWiadomość: ", m)
  kPub, kPriv = generateKey()
  c = encrypt(m, kPub)
  println("Kryptogram: ", c, "\nZdeszyfrowana wiadomość: ", decrypt(c, kPriv))
end

function messageWithSignature()
  m = 220
  println("\nWiadomość: ", m, "\nFunkcja skrótu dla wiadomości: ", hash(m))
  kPub, kPriv = generateKey()
  c = encrypt(m, kPub)
  println("Kryptogram: ", c, "\nZdeszyfrowana wiadomość: ", decrypt(c, kPriv), "\nJej funkcja skrótu: ", hash(decrypt(c, kPriv)))
end

function messageWithCRT()
  m = 2039
  println("\nWiadomość: ", m)
  kPub, kPriv = generateKey(true)
  c = encrypt(m, kPub)
  println("Kryptogram: ", c, "\nZdeszyfrowana wiadomość: ", decryptWithCRT(c, kPriv), "\n")
end

message()
messageWithSignature()
messageWithCRT()